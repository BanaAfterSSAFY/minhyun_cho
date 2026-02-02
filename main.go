package main

import (
	"context"
	"fmt"
	"log"
	"os"
	"path/filepath"
	"strings"
	"time"

	"github.com/google/generative-ai-go/genai"
	"google.golang.org/api/option"
)

func main() {
	fmt.Println("AI 리뷰어 프로세스 시작...")
	reviewDir := "reviews"

	// 1. reviews 폴더가 없으면 생성함
	if err := os.MkdirAll(reviewDir, 0755); err != nil {
		log.Fatalf("reviews 폴더 생성 실패: %v", err)
	}

	apiKey := os.Getenv("GEMINI_API_KEY")
	ctx := context.Background()
	client, err := genai.NewClient(ctx, option.WithAPIKey(apiKey))
	if err != nil {
		log.Fatalf("클라이언트 생성 실패: %v", err)
	}
	defer client.Close()

	model := client.GenerativeModel("gemini-2.5-flash")

	javaFiles := getAllJavaFiles()
	fmt.Printf("발견된 .java 파일 개수: %d개\n", len(javaFiles))

	for _, filePath := range javaFiles {
		baseName := filepath.Base(filePath)
		reviewFileName := strings.TrimSuffix(baseName, ".java")
		reviewPath := fmt.Sprintf("reviews/%s_review.md", reviewFileName)

		// 2. 중복 체크: 이미 리뷰가 존재하면 건너뜀 (할당량 절약)
		if _, err := os.Stat(reviewPath); err == nil {
			fmt.Printf("[%s] 이미 리뷰가 존재하여 건너뜀.\n", filePath)
			continue
		}

		// 3. 리뷰 프로세스 실행
		processFile(ctx, model, filePath, reviewPath)

		// 4. Rate Limit(429) 방지를 위한 15초 지연 (무료 티어 권장 사항)
		fmt.Println("다음 파일 분석 전 15초간 대기함...")
		time.Sleep(15 * time.Second)
	}
}

func getAllJavaFiles() []string {
	var javaFiles []string
	err := filepath.Walk(".", func(path string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		// .java 확장자이면서 시스템 폴더가 아닌 경우만 수집함
		isJava := strings.HasSuffix(strings.ToLower(path), ".java")
		isSystem := strings.Contains(path, "reviews") || strings.Contains(path, ".git") || info.IsDir()

		if isJava && !isSystem {
			javaFiles = append(javaFiles, path)
		}
		return nil
	})
	if err != nil {
		log.Printf("파일 탐색 중 에러: %v", err)
	}
	return javaFiles
}

func processFile(ctx context.Context, model *genai.GenerativeModel, filePath string, reviewPath string) {
	fmt.Printf("[%s] 분석 및 리뷰 중...\n", filePath)

	content, err := os.ReadFile(filePath)
	if err != nil {
		log.Printf("%s 읽기 실패: %v", filePath, err)
		return
	}

	promptText := fmt.Sprintf(`
너는 알고리즘 전문가이자 기술 블로거야. 아래 코딩 테스트 풀이(.java)를 분석해서 블로그 포스팅용 마크다운을 작성해줘.

내용 구성:
1. 문제 요약 및 풀이 전략
2. 시간 복잡도와 공간 복잡도 분석
3. 코드에서 개선할 점이나 주의해야 할 예외 케이스(Edge Case)

파일명: %s
코드 내용:
%s
`, filePath, string(content))

	resp, err := model.GenerateContent(ctx, genai.Text(promptText))
	if err != nil {
		log.Printf("%s 리뷰 생성 실패: %v", filePath, err)
		return
	}

	saveReview(filePath, reviewPath, resp)
}

func saveReview(filePath string, reviewPath string, resp *genai.GenerateContentResponse) {
	var result strings.Builder
	for _, cand := range resp.Candidates {
		if cand.Content != nil {
			for _, part := range cand.Content.Parts {
				result.WriteString(fmt.Sprintf("%v", part))
			}
		}
	}

	finalContent := fmt.Sprintf("---\ntitle: \"[리뷰] %s\"\ndate: %s\ntags: [Algorithm, Java]\n---\n\n%s", 
		filepath.Base(filePath), time.Now().Format("2006-01-02"), result.String())

	err := os.WriteFile(reviewPath, []byte(finalContent), 0644)
	if err != nil {
		log.Printf("%s 저장 실패: %v", reviewPath, err)
	} else {
		fmt.Printf("%s 저장 완료!\n", reviewPath)
	}
}