(function ($) {

    checkForLogin();
    $(document).ready(function () {
        createErrorModal();
        makeJsonRequest('/api/requests?status=ACCEPTED', 'GET', null, 'json', function (response) {
            createAcceptedRequests(response);
        }, function (err) {
            displayErrorMessage(err);
        });
    });

    function createAcceptedRequests(listRequests) {
        const header = `
        <div class="mt-3 mb-1">
            <h3>Accepted Requests:</h3>
            <hr>
        </div>
        `;
        $('#pageContent').append(header);

        const row = $("<div class='row py-2 gy-3 row-cols-4 mx-auto'></div>")
        listRequests.forEach(request => {
            const content = `
        <div class="col">
            <div class="card mb-2" style="width: 18rem; height: 420px">
              <img src="/api/img/rooms/${request.roomId}" class="card-img-top img-fluid h-100" alt="...">
              <div class="card-body">
                <p class="card-text"><b>Owner email: </b>${request.ownerEmail}</p>
                <p class="card-text"><b>Price: </b>${request.price}$</p>
                <p class="card-text"><b>Number of Guests: </b>${request.nrGuests}</p>
                <p class="card-text"><b>Start Date: </b>${request.startDate}</p>
                <p class="card-text"><b>End Date: </b>${request.endDate}</p>
              </div>
            </div>
        </div>
            `;

            row.append(content);
        });

        $('#pageContent').append(row);
        makeJsonRequest('/api/requests?status=PENDING', 'GET', null, 'json', function (res) {
            createPendingRequests(res);
        }, function (err) {
            displayErrorMessage(err);
        });
    }

    function createPendingRequests(listRequests) {
        const header = `
        <div class="mt-3 mb-1">
            <h3>Pending Requests:</h3>
            <hr>
        </div>
        `;
        $('#pageContent').append(header);

        const row = $("<div class='row py-2 gy-3 row-cols-4 mx-auto'></div>")
        listRequests.forEach(request => {
            const content = `
        <div class="col">
            <div class="card mb-2" style="width: 18rem; height: 470px">
              <img src="/api/img/rooms/${request.roomId}" class="card-img-top img-fluid h-100" alt="...">
              <div class="card-body">
                <p class="card-text"><b>Owner email: </b>${request.ownerEmail}</p>
                <p class="card-text"><b>Price: </b>${request.price}$</p>
                <p class="card-text"><b>Number of Guests: </b>${request.nrGuests}</p>
                <p class="card-text"><b>Start Date: </b>${request.startDate}</p>
                <p class="card-text"><b>End Date: </b>${request.endDate}</p>
                <a href="/api/requests/${request.id}?status=CANCEL" class="col-12 btn btn-danger processRequest">
                    <i class="bi bi-bookmark-x"></i>
                </a>
              </div>
            </div>
        </div>
            `;

            row.append(content);
        });

        $('#pageContent').append(row);
        makeJsonRequest('/api/requests?status=DECLINED', 'GET', null, 'json', function (res) {
            createDeclinedRequests(res);
        }, function (err) {
            displayErrorMessage(err);
        });
    }

    function createDeclinedRequests(listRequests) {
        const header = `
        <div class="mt-3 mb-1">
            <h3>Declined Requests:</h3>
            <hr>
        </div>
        `;
        $('#pageContent').append(header);

        const row = $("<div class='row py-2 gy-3 row-cols-4 mx-auto'></div>")
        listRequests.forEach(request => {
            const content = `
        <div class="col">
            <div class="card mb-2" style="width: 18rem; height: 420px">
              <img src="/api/img/rooms/${request.roomId}" class="card-img-top img-fluid h-100" alt="...">
              <div class="card-body">
                <p class="card-text"><b>Owner email: </b>${request.ownerEmail}</p>
                <p class="card-text"><b>Price: </b>${request.price}$</p>
                <p class="card-text"><b>Number of Guests: </b>${request.nrGuests}</p>
                <p class="card-text"><b>Start Date: </b>${request.startDate}</p>
                <p class="card-text"><b>End Date: </b>${request.endDate}</p>
              </div>
            </div>
        </div>
            `;

            row.append(content);
        });

        $('#pageContent').append(row);
        registerCancelBtnEvent();
    }

    function registerCancelBtnEvent() {
        $('.processRequest').click(function(event) {
            event.preventDefault();

            makeJsonRequest(this.href, 'POST', null, 'text', function() {
               window.location.reload();
            }, function (err) {
                displayErrorMessage(err);
            });
        });
    }

})(jQuery);