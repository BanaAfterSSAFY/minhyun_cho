package main

import (
	"context"
	"fmt"
	"log"
	"os"
	"os/exec"
	"strings"
	"time"

	"github.com/google/generative-ai-go/genai"
	"google.golang.org/api/option"
)

func main() {
	// 1. 환경 변수 및 클라이언트 설정
	apiKey := os.Getenv("GEMINI_API_KEY")
	ctx := context.Background()
	client, err := genai.NewClient(ctx, option.WithAPIKey(apiKey))
	if err != nil {
		log.Fatalf("클라이언트 생성 실패: %v", err)
	}
	defer client.Close()

	model := client.GenerativeModel("gemini-pro")

	// 2. 이번 커밋에서 변경된 .java 파일 목록 가져오기
	changedFiles := getChangedJavaFiles()
	if len(changedFiles) == 0 {
		fmt.Println("리뷰할 새로운 .java 파일이 없음.")
		return
	}

	// 3. 각 파일별로 리뷰 진행
	for _, filePath := range changedFiles {
		processFile(ctx, model, filePath)
	}
}

func getChangedJavaFiles() []string {
	// 마지막 커밋과 그 이전 커밋의 차이를 비교하여 파일명만 추출함
	out, err := exec.Command("git", "diff", "--name-only", "HEAD^", "HEAD").Output()
	if err != nil {
		return nil
	}

	allFiles := strings.Split(string(out), "\n")
	var javaFiles []string
	for _, f := range allFiles {
		f = strings.TrimSpace(f)
		// .java 확장자이고 파일이 실제로 존재하는 경우만 포함함
		if strings.HasSuffix(f, ".java") {
			if _, err := os.Stat(f); err == nil {
				javaFiles = append(javaFiles, f)
			}
		}
	}
	return javaFiles
}

func processFile(ctx context.Context, model *genai.GenerativeModel, filePath string) {
	fmt.Printf("%s 파일 리뷰 중...\n", filePath)

	content, err := os.ReadFile(filePath)
	if err != nil {
		return
	}

	// AI에게 전달할 프롬프트 구성
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

	// 결과 저장
	saveReview(filePath, resp)
}

func saveReview(filePath string, resp *genai.GenerateContentResponse) {
	reviewDir := "reviews"
	if _, err := os.Stat(reviewDir); os.IsNotExist(err) {
		os.Mkdir(reviewDir, 0755)
	}

	// 파일명에서 확장자 제거 후 _review.md 붙임
	baseName := strings.TrimSuffix(filePath, ".java")
	reviewPath := fmt.Sprintf("%s/%s_review.md", reviewDir, baseName)

	result := ""
	for _, cand := range resp.Candidates {
		for _, part := range cand.Content.Parts {
			result += fmt.Sprintf("%v", part)
		}
	}

	// 메타데이터 추가 (블로그용)
	finalContent := fmt.Sprintf("---\ntitle: \"[리뷰] %s\"\ndate: %s\ntags: [Algorithm, Java]\n---\n\n%s", 
		filePath, time.Now().Format("2006-01-02"), result)

	err := os.WriteFile(reviewPath, []byte(finalContent), 0644)
	if err != nil {
		log.Printf("파일 저장 실패: %v", err)
	}
}