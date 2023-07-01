// 삭제버튼
document.addEventListener('DOMContentLoaded', function() {
    // Delete function
    const deleteButton = document.getElementById('delete-btn');
    if (deleteButton) {
        deleteButton.addEventListener('click', event => {
            let id = document.getElementById('article-id').value;
            fetch(`/api/articles/${id}`, {
                method: 'DELETE'
            })
                .then(() => {
                    alert('삭제가 완료되었습니다.');
                    location.replace('/articles');
                });
        });
    }

    // 수정버튼
    const modifyButton = document.getElementById('modify-btn');
    if (modifyButton) {
        modifyButton.addEventListener('click', event => {
            let params = new URLSearchParams(location.search);
            let id = params.get('id');

            fetch(`/api/articles/${id}`, {
                method: 'PUT',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    title: document.getElementById('title').value,
                    content: document.getElementById('content').value
                })
            })
                .then(() => {
                    alert('수정이 완료되었습니다.');
                    location.replace(`/articles/${id}`);
                });
        });
    }

    // 등록 버튼
    const createButton = document.getElementById('create-btn');
    if (createButton) {
        createButton.addEventListener('click', event => {
            let title = document.getElementById('title').value.trim();
            let content = document.getElementById('content').value.trim();

            if (title === '' || content === '') {
                event.preventDefault();

                // 경고창
                alert('제목, 내용을 입력해주세요');

                // Redirect to /new-article
                window.location.href = '/new-article';
            } else {
                // Proceed with form submission
                fetch('/api/articles', {
                    method: 'POST',
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        title: title,
                        content: content
                    })
                })
                    .then(() => {
                        alert('등록 완료되었습니다.');
                        location.replace('/articles');
                    });
            }
        });
    }

});
