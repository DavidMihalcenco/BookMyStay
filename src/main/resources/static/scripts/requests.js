(function ($) {

    checkForLogin();
    $(document).ready(function () {
        createErrorModal();
        makeJsonRequest('/api/requests?status=PENDING&isModerator=true', 'GET', null, 'json', function (response) {
            createPendingRequests(response);
        }, function (err) {
            displayErrorMessage(err);
        });
    });

    function createPendingRequests(listRequests) {
        const header = `
        <div class="mt-3 mb-1">
            <h3>Requests in process:</h3>
            <hr>
        </div>
        `;
        $('#pageContent').append(header);

        const row = $("<div class='row py-2 gy-3 row-cols-4 mx-auto'></div>")
        listRequests.forEach(request => {
            const content = `
        <div class="col">
            <div class="card mb-2" style="width: 18rem; height: 500px">
              <img src="/api/img/rooms/${request.roomId}" class="card-img-top img-fluid h-100" alt="...">
              <div class="card-body">
                <p class="card-text"><b>Guest email: </b>${request.guestEmail}</p>
                <p class="card-text"><b>Price: </b>${request.price}$</p>
                <p class="card-text"><b>Number of Guests: </b>${request.nrGuests}</p>
                <p class="card-text"><b>Start Date: </b>${request.startDate}</p>
                <p class="card-text"><b>End Date: </b>${request.endDate}</p>
                <div class="row">
                    <div class="col-6">
                        <a href="/api/requests/${request.id}?status=ACCEPTED" class="col-12 btn btn-success processRequest">
                            <i class="bi bi-calendar2-check-fill"></i>
                        </a>
                    </div>
                    <div class="col-6">
                        <a href="/api/requests/${request.id}?status=DECLINED" class="col-12 btn btn-danger processRequest">
                            <i class="bi bi-calendar2-x-fill"></i>
                        </a>
                    </div>
                </div>
              </div>
            </div>
        </div>
            `;

            row.append(content);
        });

        $('#pageContent').append(row);
        makeJsonRequest('/api/requests?status=ACCEPTED&isModerator=true', 'GET', null, 'json', function (res) {
            createAcceptedRequests(res);
        }, function (err) {
            displayErrorMessage(err);
        });
    }

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
                <p class="card-text"><b>Guest email: </b>${request.guestEmail}</p>
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
        makeJsonRequest('/api/requests?status=DECLINED&isModerator=true', 'GET', null, 'json', function (res) {
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
                <p class="card-text"><b>Guest email: </b>${request.guestEmail}</p>
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
        registerRequestProcessEvent();
    }

    function registerRequestProcessEvent() {
        $('.processRequest').click(function (event) {
           event.preventDefault();

           makeJsonRequest(this.href, 'POST', null, 'text', function () {
              window.location.reload();
           }, function (err) {
               displayErrorMessage(err);
           });
        });
    }

})(jQuery);