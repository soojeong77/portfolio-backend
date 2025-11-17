// guestbook.js

// Spring Boot 서버의 기본 URL (메모리 기반 API 사용)
const API_URL = 'http://localhost:8080/api/guestbook';
const guestbookList = document.getElementById('guestbookList');
const guestbookForm = document.getElementById('guestbookForm');

// ----------------------------------------------------
// 1. 방명록 목록 조회 (READ)
// ----------------------------------------------------
async function fetchGuestbooks() {
    try {
        guestbookList.innerHTML = '<p style="text-align: center; color: #777; padding: 20px;">데이터를 불러오는 중...</p>';
        
        // GET 요청으로 API에서 데이터 가져오기
        const response = await fetch(API_URL);
        const messages = await response.json(); // JSON 응답을 객체로 변환
        
        renderGuestbooks(messages);

    } catch (error) {
        console.error('방명록 데이터를 불러오는 중 오류 발생:', error);
        guestbookList.innerHTML = '<p style="text-align: center; color: red; padding: 20px;">방명록 서버 연결에 실패했습니다. (Spring Boot 서버가 실행 중인지 확인하세요.)</p>';
    }
}

// ----------------------------------------------------
// 2. 방명록 목록 HTML 렌더링
// ----------------------------------------------------
function renderGuestbooks(messages) {
    if (messages.length === 0) {
        guestbookList.innerHTML = '<p style="text-align: center; color: #777; padding: 20px;">아직 작성된 방명록이 없습니다. 첫 글을 남겨주세요!</p>';
        return;
    }

    guestbookList.innerHTML = ''; // 기존 목록 초기화
    
    // 데이터를 최신순으로 정렬하여 표시합니다.
    messages.sort((a, b) => new Date(b.regDate) - new Date(a.regDate));

    messages.forEach(msg => {
        // regDate가 없는 경우 현재 시간으로 대체 (메모리 기반 저장소 문제 해결)
        const validDate = msg.regDate || new Date().toISOString(); 
        const date = new Date(validDate).toLocaleString('ko-KR', {
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

// ----------------------------------------------------
// 3. 방명록 작성 처리 (CREATE)
// ----------------------------------------------------
if (guestbookForm) {
    guestbookForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const author = document.getElementById('author').value.trim();
        const content = document.getElementById('content').value.trim();
        const password = document.getElementById('password').value;

        const data = { author, content, password };

        try {
            const response = await fetch(API_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data) 
            });

            if (response.ok) {
                alert('방명록이 성공적으로 작성되었습니다!');
                guestbookForm.reset();
                fetchGuestbooks(); // 목록 갱신
            } else {
                alert('작성에 실패했습니다. 서버 로그를 확인하세요.');
            }

        } catch (error) {
            console.error('방명록 작성 중 오류 발생:', error);
            alert('서버 연결 문제로 작성에 실패했습니다.');
        }
    });
}


// ----------------------------------------------------
// 4. 방명록 삭제 처리 (DELETE)
// ----------------------------------------------------
async function deleteGuestbook(gId) {
    const password = prompt('삭제용 비밀번호를 입력하세요:');
    if (!password) return; 

    try {
        const response = await fetch(`${API_URL}/${gId}`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ password: password })
        });

        if (response.ok) {
            alert('방명록이 삭제되었습니다.');
            fetchGuestbooks(); // 목록 갱신
        } else if (response.status === 404) {
             alert('삭제 권한이 없습니다: ID 또는 비밀번호가 일치하지 않습니다.');
        } else {
            // 다른 5xx 에러도 처리 (메모리 기반에서는 주로 404가 발생)
            alert('삭제에 실패했습니다. 서버 오류 또는 권한 문제일 수 있습니다. (코드:' + response.status + ')');
        }

    } catch (error) {
        console.error('방명록 삭제 중 오류 발생:', error);
        alert('서버 연결 문제로 삭제에 실패했습니다.');
    }
}