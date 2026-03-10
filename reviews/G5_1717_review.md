---
title: "[리뷰] G5_1717.java"
date: 2026-03-10
tags: [Algorithm, Java]
---

안녕하세요, 알고리즘과 자료구조의 세계를 탐험하는 여러분! 🚀 오늘은 백준 온라인 저지 1717번, '집합의 표현' 문제를 Union-Find 자료구조를 활용하여 효율적으로 해결하는 방법에 대해 깊이 있게 분석해보겠습니다. 이 문제는 Union-Find의 개념과 그 최적화 기법을 이해하는 데 아주 좋은 예시입니다.

---

## [백준 1717] 집합의 표현: Union-Find 마스터하기

![Union-Find Image](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fe2iXQ7%2FbtqG41zK2gQ%2FKK2w5tSg7KkO6mY5vWkr11%2Fimg.png)
*(이미지 출처: 일반적으로 Union-Find 자료구조를 설명하는 다이어그램)*

### 1. 문제 요약 및 풀이 전략

**문제 요약:**
`n+1`개의 집합이 있습니다. 각각은 `0`부터 `n`까지의 숫자를 원소로 가지며, 초기에는 모든 원소가 각자 하나의 독립적인 집합을 이룹니다. 총 `m`개의 연산이 주어지며, 각 연산은 다음 두 가지 중 하나입니다.
1.  `0 a b`: `a`가 포함된 집합과 `b`가 포함된 집합을 합칩니다 (Union).
2.  `1 a b`: `a`와 `b`가 같은 집합에 포함되어 있는지 확인합니다 (Find). 같은 집합이면 "YES", 아니면 "NO"를 출력합니다.

**풀이 전략:**
이 문제는 전형적인 **Union-Find (또는 Disjoint Set)** 자료구조를 사용하여 해결할 수 있습니다. Union-Find는 여러 개의 원소들을 그룹으로 묶거나, 어떤 두 원소가 같은 그룹에 속해 있는지를 판별하는 데 최적화된 자료구조입니다.

핵심 아이디어는 다음과 같습니다.
1.  **`parent` 배열:** 각 원소의 부모를 저장하는 배열입니다. `parent[i] = i`인 경우, `i`는 자신의 집합의 '대표 원소(루트)'임을 나타냅니다.
2.  **`find(x)` 연산:** 원소 `x`가 속한 집합의 대표 원소(루트)를 찾아 반환합니다. 이 과정에서 **경로 압축(Path Compression)**을 적용하여 성능을 극대화합니다. 경로 압축은 `x`가 루트를 찾아가는 경로상의 모든 원소들이 직접 루트를 가리키도록 부모를 갱신하여, 다음 탐색 시 시간을 단축하는 기법입니다.
3.  **`union(x, y)` 연산:** 원소 `x`가 속한 집합과 원소 `y`가 속한 집합을 합칩니다. 이때, 각 집합의 루트를 찾아 두 루트 중 하나를 다른 하나의 자식으로 만듭니다. 이 과정에서 **Union by Rank (또는 Union by Size)** 최적화를 적용합니다. 이는 트리의 높이(rank)를 기준으로 낮은 높이의 트리를 높은 높이의 트리에 연결하여, 트리의 불균형한 성장을 막아 전체적인 트리의 높이를 낮게 유지하는 기법입니다.

### 2. 시간 복잡도와 공간 복잡도 분석

제공된 코드는 Union-Find의 두 가지 핵심 최적화 기법인 **경로 압축(Path Compression)**과 **Union by Rank**를 모두 적용하고 있습니다.

*   **공간 복잡도 (Space Complexity):**
    *   `parent` 배열: `N+1` 크기로 `O(N)`의 공간을 사용합니다.
    *   `rank` 배열: `N+1` 크기로 `O(N)`의 공간을 사용합니다.
    *   총 공간 복잡도는 **`O(N)`**입니다.

*   **시간 복잡도 (Time Complexity):**
    *   **초기화:** `parent`와 `rank` 배열을 초기화하는 데 `N+1`번의 연산이 필요하므로 `O(N)`입니다.
    *   **`M`개의 연산:** `find` 및 `union` 연산은 경로 압축과 Union by Rank가 적용되면, 거의 상수 시간에 가까운 **아모타이즈드 시간 복잡도(Amortized Time Complexity)**를 가집니다. 이는 역 아커만 함수(Inverse Ackermann function) `α(N)`으로 표현됩니다. `α(N)`은 `N`이 매우 커지더라도 그 값이 4~5를 넘지 않는 매우 느리게 증가하는 함수이므로, 사실상 상수 시간으로 간주할 수 있습니다.
    *   따라서, 전체 시간 복잡도는 **`O(N + Mα(N))`** 입니다. 대부분의 경우 `α(N)`을 상수로 취급하여 **`O(N + M)`**으로 근사하여 말합니다.
    *   입출력 부분에서 `BufferedReader`와 `StringTokenizer`를 사용하여 표준 입출력 속도를 최적화했습니다. 이는 많은 수의 입출력이 발생하는 코딩 테스트에서 필수적인 요소입니다.

이러한 최적화 덕분에 Union-Find는 매우 빠르고 효율적으로 동작하며, `N`과 `M`이 최대 1,000,000에 달하는 문제들도 해결할 수 있습니다.

### 3. 코드에서 개선할 점이나 주의해야 할 예외 케이스(Edge Case)

제공된 코드는 Union-Find의 모범적인 구현 사례라고 할 수 있습니다. 대부분의 코딩 테스트 환경에서 이 정도면 완벽에 가깝습니다. 하지만 몇 가지 측면에서 더 고민해 볼 부분이 있습니다.

**개선할 점 (Improvements):**

1.  **클래스 캡슐화:** 현재 `parent`와 `rank` 배열, 그리고 `find`, `union` 메서드가 모두 `static`으로 선언되어 있습니다. 이는 `main` 메서드 안에서 바로 사용하기 편리하지만, 객체 지향적인 관점에서는 `UnionFind` 클래스를 별도로 정의하고 이 안에 멤버 변수와 메서드를 포함시키는 것이 더 좋습니다.
    ```java
    // 개선된 UnionFind 클래스 예시
    class UnionFind {
        private int[] parent;
        private int[] rank;

        public UnionFind(int n) {
            parent = new int[n + 1];
            rank = new int[n + 1];
            for (int i = 0; i <= n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        public int find(int x) {
            if (parent[x] == x) return x;
            return parent[x] = find(parent[x]);
        }

        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX != rootY) {
                if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
                return true;
            }
            return false;
        }
    }
    ```
    이렇게 구현하면 여러 개의 Union-Find 인스턴스를 관리해야 할 때 유연하게 대처할 수 있으며, 코드의 재사용성과 유지보수성이 높아집니다.

2.  **`union` 메서드의 반환 값 활용:** 현재 `union` 메서드는 `boolean` 값을 반환하지만, `main` 메서드에서는 이 반환 값을 사용하지 않습니다. `union` 연산이 실제로 두 집합을 합쳤는지 (`true`) 아니면 이미 같은 집합이었는지 (`false`)를 알 수 있게 해주므로, 특정 로직에서는 유용하게 사용될 수 있습니다. 이 문제에서는 필수적이지 않습니다.

3.  **Union by Rank vs. Union by Size:** 코드에서는 Union by Rank를 사용하고 있습니다. Union by Size (각 집합의 원소 개수를 기록)도 비슷한 성능 향상을 가져오며, 구현 방식에 따라서는 `rank` 대신 `size`를 갱신하는 것이 더 직관적일 수도 있습니다. 어떤 것을 사용하든 성능은 동일하게 최적화됩니다.

**주의해야 할 예외 케이스 (Edge Cases):**

1.  **`N = 0` 또는 `M = 0`:**
    *   `N = 0`: `parent = new int[1]`, `rank = new int[1]`로 생성됩니다. 루프에서 `i=0`만 돌며 `parent[0]=0`, `rank[0]=0`으로 초기화됩니다. 이는 문제의 최소 `N` 제약(보통 `N >= 1`)을 고려하면 실제 발생하기 어렵지만, 코드는 `N=0`일 때도 오류 없이 동작합니다.
    *   `M = 0`: 연산 루프가 한 번도 실행되지 않고 프로그램이 종료됩니다. 올바른 동작입니다.

2.  **`a == b`인 경우:**
    *   `union(a, a)`: `find(a)`는 `a`를 반환하고, `rootX`와 `rootY`가 같으므로 `if (rootX != rootY)` 조건에 걸려 아무 작업도 수행하지 않습니다. 올바른 동작입니다.
    *   `1 a a`: `find(a)`는 `a`를 반환하고, `parentA`와 `parentB`가 같으므로 "YES"를 출력합니다. 올바른 동작입니다.

3.  **입력 범위 (0부터 N):** 문제에서 원소의 번호가 `0`부터 `N`까지라고 명시되어 있으며, 코드는 `parent`와 `rank` 배열을 `N+1` 크기로 할당하여 인덱스 `0`부터 `N`까지 모두 사용할 수 있도록 했습니다. 이는 문제의 명세에 정확히 부합하며, 인덱스 아웃 오브 바운드(Index Out Of Bounds) 예외를 방지합니다.

---

### 마무리하며

'집합의 표현' 문제는 Union-Find 자료구조의 가장 기본적인 활용법을 묻는 문제입니다. 경로 압축과 Union by Rank/Size와 같은 최적화 기법을 적용하면 수많은 연산 속에서도 `O(N+M)`에 가까운 놀라운 성능을 발휘할 수 있습니다. 코딩 테스트에서 이러한 문제를 만나면, 주저 없이 Union-Find를 떠올리시고 이 코드와 같은 최적화된 구현 방식을 사용하시길 강력히 추천합니다!

이 글이 Union-Find를 이해하고 문제 해결 능력을 향상시키는 데 도움이 되기를 바랍니다. 다음에도 더 흥미로운 알고리즘과 자료구조로 찾아오겠습니다! Happy Coding! ✨