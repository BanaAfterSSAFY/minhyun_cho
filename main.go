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
	apiKey := os.Getenv("GEMINI_API_KEY")
	ctx := context.Background()
	client, err := genai.NewClient(ctx, option.WithAPIKey(apiKey))
	if err != nil {
		log.Fatalf("클라이언트 생성 실패: %v", err)
	}
	defer client.Close()

	model := client.GenerativeModel("gemini-2.5-pro")

	// 1. 기존 증분 추적 로직 (주석 처리)
	/*
	out, _ := exec.Command("git", "diff", "--name-only", "HEAD^", "HEAD").Output()
	allFiles := strings.Split(string(out), "\n")
	*/

	// 2. 레포지토리 내 모든 .java 파일 가져오기
	javaFiles := getAllJavaFiles()
	if len(javaFiles) == 0 {
		fmt.Println("리뷰할 .java 파일이 없음.")
		return
	}

	for _, filePath := range javaFiles {
		processFile(ctx, model, filePath)
	}
}

func getAllJavaFiles() []string {
	var javaFiles []string
	// 현재 폴더(.)부터 하위 폴더까지 모든 파일을 탐색함
	err := filepath.Walk(".", func(path string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		// 파일이면서 .java 확장자인 경우만 리스트에 추가함
		// (단, reviews 폴더 안의 파일이나 숨김 폴더는 제외함)
		if !info.IsDir() && strings.HasSuffix(path, ".java") && !strings.Contains(path, "reviews") {
			javaFiles = append(javaFiles, path)
		}
		return nil
	})
	if err != nil {
		log.Printf("파일 탐색 중 에러 발생: %v", err)
	}
	return javaFiles
}

func processFile(ctx context.Context, model *genai.GenerativeModel, filePath string) {
	fmt.Printf("%s 파일 분석 및 리뷰 중...\n", filePath)

	content, err := os.ReadFile(filePath)
	if err != nil {
		log.Printf("%s 파일 읽기 실패: %v", filePath, err)
		return
	}

	promptText := fmt.Sprintf(`
너는 알고리즘 전문가이자 기술 블로거야. 아래 코딩 테스트 풀이(.java)를 분석해서 블로그 포스팅용 마크다운을 작성해줘.

내용 구성:
1. 문제 요약 및 풀이 전략
2. 시간 복잡도와 공간 복잡도 분석
3. 코드에서 개선할 점이나 주의해야 할 예외 케이스(Edge Case)

파일 이름: %s
코드 내용:
%s
`, filePath, string(content))

	resp, err := model.GenerateContent(ctx, genai.Text(promptText))
	if err != nil {
		log.Printf("%s 리뷰 생성 실패: %v", filePath, err)
		return
	}

	saveReview(filePath, resp)
}

func saveReview(filePath string, resp *genai.GenerateContentResponse) {
	reviewDir := "reviews"
	// reviews 폴더가 없으면 생성함
	if _, err := os.Stat(reviewDir); os.IsNotExist(err) {
		err := os.MkdirAll(reviewDir, 0755)
		if err != nil {
			log.Printf("폴더 생성 실패: %v", err)
			return
		}
	}

	// 파일 경로에서 파일명만 추출하여 저장용 이름 생성함
	baseName := filepath.Base(filePath)
	reviewFileName := strings.TrimSuffix(baseName, ".java")
	reviewPath := fmt.Sprintf("%s/%s_review.md", reviewDir, reviewFileName)

	var result strings.Builder
	for _, cand := range resp.Candidates {
		for _, part := range cand.Content.Parts {
			result.WriteString(fmt.Sprintf("%v", part))
		}
	}

	finalContent := fmt.Sprintf("---\ntitle: \"[리뷰] %s\"\ndate: %s\ntags: [Algorithm, Java]\n---\n\n%s", 
		baseName, time.Now().Format("2006-01-02"), result.String())

	err := os.WriteFile(reviewPath, []byte(finalContent), 0644)
	if err != nil {
		log.Printf("파일 저장 실패: %v", err)
	}
}