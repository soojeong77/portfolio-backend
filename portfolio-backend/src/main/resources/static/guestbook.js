// **수정된 API 주소**입니다. 백엔드 서비스의 Render URL을 사용해야 합니다.
// 백엔드 URL: https://portfolio-backend-787n.onrender.com (로그에서 확인된 URL)
const API_URL = 'https://portfolio-backend-787n.onrender.com/api/guestbook';

// 프론트엔드 코드의 나머지 부분은 그대로 유지됩니다.

document.addEventListener('DOMContentLoaded', () => {
    const guestbookList = document.getElementById('guestbookList');
    const guestbookForm = document.getElementById('guestbookForm');

    // 방명록 목록 조회
    async function fetchGuestbooks() {
        try {
            // 로딩 상태 메시지 표시
            guestbookList.innerHTML = '<p style="text-align: center; color: #777; padding: 20px;">데이터를 불러오는 중...</p>';
            
            // Render 서버로 요청을 보냅니다.
            const response = await fetch(API_URL, { credentials: 'include' });
            
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
            
            const messages = await response.json();
            renderGuestbooks(messages);
        } catch (error) {
            console.error('방명록 데이터를 불러오는 중 오류 발생:', error);
            // 오류 시 표시되는 메시지
            guestbookList.innerHTML = '<p style="text-align: center; color: red; padding: 20px;">방명록 서버 연결에 실패했습니다. (주소 오류 또는 콜드 스타트)</p>';
        }
    }

    // 방명록 렌더링
    function renderGuestbooks(messages) {
        if (!messages || messages.length === 0) {
            guestbookList.innerHTML = '<p style="text-align: center; color: #777; padding: 20px;">아직 작성된 방명록이 없습니다!</p>';
            return;
        }

        guestbookList.innerHTML = '';
        messages.sort((a, b) => new Date(b.regDate) - new Date(a.regDate));

        messages.forEach(msg => {
            const date = new Date(msg.regDate).toLocaleString('ko-KR', {
                year: 'numeric', month: '2-digit', day: '2-digit',
                hour: '2-digit', minute: '2-digit'
            });

            const msgDiv = document.createElement('div');
            msgDiv.className = 'guestbook-entry';
            msgDiv.innerHTML = `
                <div class="entry-header">
                    <strong>${msg.author}</strong>
                    <span class="entry-date">(${date})</span>
                    <button class="delete-btn" onclick="deleteGuestbook(${msg.gId})">삭제</button>
                </div>
                <p class="content">${msg.content}</p>
            `;
            guestbookList.appendChild(msgDiv);
        });
    }

    // 방명록 작성
    if (guestbookForm) {
        guestbookForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const author = document.getElementById('author').value.trim();
            const content = document.getElementById('content').value.trim();
            const password = document.getElementById('password').value;

            try {
                const response = await fetch(API_URL, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    credentials: 'include',
                    body: JSON.stringify({ author, content, password })
                });

                if (response.ok) {
                    alert('방명록이 작성되었습니다!'); 
                    guestbookForm.reset();
                    fetchGuestbooks();
                } else {
                    alert('작성 실패: 서버 로그 확인');
                }
            } catch (error) {
                console.error('방명록 작성 중 오류 발생:', error);
                alert('방명록 작성 중 오류가 발생했습니다.');
            }
        });
    }

    // 초기 방명록 목록 불러오기
    fetchGuestbooks();

    // 삭제 함수 전역에 노출 (버튼 onclick용)
    window.deleteGuestbook = async function(gId) {
        const password = prompt('삭제용 비밀번호를 입력하세요:'); 
        if (!password) return;

        try {
            const response = await fetch(`${API_URL}/${gId}?password=${encodeURIComponent(password)}`, {
                method: 'DELETE',
                credentials: 'include'
            });

            if (response.ok) {
                alert('방명록이 삭제되었습니다.');
                fetchGuestbooks();
            } else {
                alert('삭제 실패: 비밀번호가 맞지 않거나 서버 오류');
            }
        } catch (error) {
            console.error('방명록 삭제 중 오류 발생:', error);
            alert('방명록 삭제 중 오류가 발생했습니다.');
        }
    };
});