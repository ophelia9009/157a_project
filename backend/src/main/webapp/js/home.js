document.addEventListener('DOMContentLoaded', function() {
    // Handle Create Subforum Modal with Bootstrap
    const modal = new bootstrap.Modal(document.getElementById('subforumModal'));
    const btn = document.getElementById('createSubforumBtn');

    btn.onclick = function() {
        modal.show();
    }

    // Handle Subforum Creation
    document.getElementById('subforumForm').addEventListener('submit', function(e) {
        e.preventDefault();

        const name = document.getElementById('subforumName').value;
        const description = document.getElementById('subforumDesc').value;

        fetch('/backend/api/subforums', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                name: name,
                description: description
            })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to create subforum');
            }
            return response.json();
        })
        .then(data => {
            // Show success message with Bootstrap toast
            const toastContainer = document.createElement('div');
            toastContainer.className = 'position-fixed bottom-0 end-0 p-3';
            toastContainer.style.zIndex = '11';
            toastContainer.innerHTML = `
                <div class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true">
                    <div class="d-flex">
                        <div class="toast-body">
                            Subforum created successfully!
                        </div>
                        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                    </div>
                </div>
            `;
            document.body.appendChild(toastContainer);
            const toastEl = toastContainer.querySelector('.toast');
            const toast = new bootstrap.Toast(toastEl);
            toast.show();
            
            modal.hide();
            setTimeout(() => window.location.reload(), 1500);
        })
        .catch(error => {
            alert('Error: ' + error.message);
        });
    });

    document.getElementById('filterForm').addEventListener('submit', function (e) {
        e.preventDefault(); // Prevent default form submission

        const formData = new FormData(this);
        const query = new URLSearchParams();

        for (const [key, value] of formData.entries()) {
            if (value) query.append(key, value);
        }

        fetch('/backend/api/subforumsearch?' + query.toString())
            .then(res => {
                if (!res.ok) throw new Error("Failed to fetch subforums");
                return res.json();
            })
            .then(data => {
                const resultsDiv = document.getElementById('results');
                resultsDiv.innerHTML = '';

                if (data.length === 0) {
                    resultsDiv.innerHTML = '<div class="alert alert-info">No results found.</div>';
                    return;
                }

                const row = document.createElement('div');
                row.className = 'row';
                
                data.forEach(subforum => {
                    const col = document.createElement('div');
                    col.className = 'col-md-4 mb-3';
                    col.innerHTML = `
                        <div class="card h-100">
                            <div class="card-header">
                                <h5 class="card-title">${subforum.name}</h5>
                            </div>
                            <div class="card-body">
                                <p class="card-text">${subforum.description}</p>
                            </div>
                            <ul class="list-group list-group-flush">
                                <li class="list-group-item">Subscribers: ${subforum.subscriberCount}</li>
                                <li class="list-group-item">Created: ${subforum.creationDate}</li>
                                <li class="list-group-item">Updated: ${subforum.lastUpdated}</li>
                            </ul>
                        </div>
                    `;
                    row.appendChild(col);
                });
                
                resultsDiv.appendChild(row);
            })
            .catch(err => {
                document.getElementById('results').innerHTML = `<div class="alert alert-danger">${err.message}</div>`;
            });
    });
});
