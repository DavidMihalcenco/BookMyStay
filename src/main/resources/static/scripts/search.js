(function ($) {

    $(document).ready(function () {
        createBookRoomModal();
        createBookErrorModal();
        createFilterOptions();
    });

    function createBookErrorModal() {
        const content = `
        <div class="modal fade" id="errorBookModal">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <button id="errorModalCloseBtn" class="btn-close"></button>
                    </div>
                    <div class="modal-body text-center">
                        <h2 id="error-message"></h2>
                    </div>
                </div>
            </div>
        </div>
        `;

        $('#pageContent').append(content);
        registerCloseErrorModalEvent();
    }

    function registerCloseErrorModalEvent() {
        $('#errorModalCloseBtn').click(function () {
            $('#errorBookModal').modal('hide');
            $('#searchForm').submit();
        });
    }

    function createBookRoomModal() {
        const content = `
        <div class="modal fade" id="bookRoomModal">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <button id="modalCloseBtn" class="btn-close" data-bs-dismiss="modal" data-bs-target="#bookRoomModal"></button>
                    </div>
                    <div class="modal-body">
                        <h4>Are you sure you want to book this room?</h4>
                        <h5><b>Check in: </b><span id="checkInModal"></span></h5>
                        <h5><b>Check out: </b> <span id="checkOutModal"></span></h5>
                    </div>
                    <div class="modal-footer">
                        <a class="btn btn-success" id="createRequestBtn">Yes</a>
                        <button class="btn btn-danger" data-bs-dismiss="modal" data-bs-target="#bookRoomModal">No</button>
                    </div>
                </div>
            </div>
        </div>
        `;

        $('#pageContent').append(content);
        registerCreateRequestEvent();
    }

    function registerCreateRequestEvent() {
        $('#createRequestBtn').click(function (e) {
           e.preventDefault();

           const url = $('#createRequestBtn').attr('href');
           const id = url.substring(url.lastIndexOf('/') + 1);
           const data = {
               startDate: $('#checkInModal').text(),
               endDate: $('#checkOutModal').text(),
           }
           makeJsonRequest(url, 'POST', data, 'text', function () {
               window.location.href = '/myBookings';
           }, function (err) {
              $('#error-message').text(err.responseText);
              $('#bookRoomModal').modal('hide');
              $('#errorBookModal').modal('show');
           });
        });
    }

    function createFilterOptions() {
        const tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);
        const tomorrowISO = tomorrow.toISOString().split("T")[0];

        const oneYearFromTomorrow = new Date();
        oneYearFromTomorrow.setDate(oneYearFromTomorrow.getDate() + 1);
        oneYearFromTomorrow.setFullYear(oneYearFromTomorrow.getFullYear() + 1);
        const oneYearFromTomorrowISO = oneYearFromTomorrow.toISOString().split("T")[0];

        const dayAfterTomorrow = new Date();
        dayAfterTomorrow.setDate(dayAfterTomorrow.getDate() + 2);
        const dayAfterTomorrowISO = dayAfterTomorrow.toISOString().split("T")[0];

        const oneYearFromDayAfterTomorrow = new Date();
        oneYearFromDayAfterTomorrow.setDate(dayAfterTomorrow.getDate() + 1);
        oneYearFromDayAfterTomorrow.setFullYear(oneYearFromDayAfterTomorrow.getFullYear() + 1);
        const oneYearFromDayAfterTomorrowISO = oneYearFromDayAfterTomorrow.toISOString().split("T")[0];


        const content = `
<div class="card my-3 py-3">
    <form id="searchForm" class="row align-items-center justify-content-center" novalidate>
        <div class="col-2">
            <input type="text" placeholder="Location" class="form-control" id="locationInput" minlength="2" maxlength="70">
        </div>
        <div class="col-2">
            <input type="date" placeholder="in" id="checkInInput" class="form-control" min=${tomorrowISO} max=${oneYearFromTomorrowISO} value="${tomorrowISO}" required>
        </div>
        <div class="col-2">
            <input type="date" placeholder="out" id="checkOutInput" class="form-control" min=${dayAfterTomorrowISO} max=${oneYearFromDayAfterTomorrowISO} value="${dayAfterTomorrowISO}" required>
        </div> 
        <div class="col-4">
            <div class="input-group bg-white border-0">
                <div class="input-group-text">$</div>
                <input type="number" placeholder="0" class="form-control" id="minPriceInput" min="1" max="4999">
                <input type="number" placeholder="5000" class="form-control" id="maxPriceInput" min="2" max="5000">
                <div class="input-group-text"><i class="bi bi-people-fill"></i></div>
                <input type="number" class="form-control" id="nrGuestsInput" placeholder="Guests" min="1" max="30">
            </div>
        </div>  
        <div class="col-1">
            <div class="input-group bg-white border-0">
                <button id="searchBtn" type="submit" class="btn btn-warning"><i class="bi bi-search"></i></button>
                <button id="resetBtn" type="reset" class="btn btn-secondary"><i class="bi bi-arrow-counterclockwise"></i></button>
            </div>
        </div>
    </form>
</div>
        `;

        $('#pageContent').append(content);
        registerSearchSubmitEvent();
        registerCheckInChangeEvent();
        registerResetBtnEvent();
    }

    function registerResetBtnEvent() {
        $('#resetBtn').click(function (e){
            e.preventDefault();
            e.stopPropagation();

            $('#locationInput').val('')
            $('#minPriceInput').val('')
            $('#maxPriceInput').val('')
            $('#nrGuestsInput').val('')
            $('#searchForm').submit();
        })
    }

    function registerCheckInChangeEvent() {
        let startDateInput = $('#checkInInput');
        let endDateInput = $('#checkOutInput');

        startDateInput.on('change', function() {
            let startDate = new Date(startDateInput.val());
            let endDate = new Date(endDateInput.val());

            let newEndDate = new Date(startDate.getTime() + (24 * 60 * 60 * 1000));
            let newEndDateISO = newEndDate.toISOString().substring(0, 10);
            endDateInput.attr('min', newEndDateISO);

            if (startDate >= endDate) {
                endDateInput.val(newEndDateISO);
            }
        });
    }

    function registerSearchSubmitEvent() {

        $('#searchForm').submit(function(e) {
            const form = $('#searchForm')[0];
            e.preventDefault();

            form.classList.add('was-validated');
            if (!form.checkValidity())
                return

            createListOfRooms();
        });
    }

    function createListOfRooms() {
        const checkIn = $('#checkInInput').val()
        const checkOut = $('#checkOutInput').val()
        const location = $('#locationInput').val()
        let min = $('#minPriceInput').val()
        let max = $('#maxPriceInput').val()
        let nr = $('#nrGuestsInput').val()

        if (!min)
            min = 1
        if (!max)
            max = 5000
        if (!nr)
            nr = 1

        const url = `/api/search?min=${min}&max=${max}&nrOfGuests=${nr}&location=${location}&startDate=${checkIn}&endDate=${checkOut}`
        makeJsonRequest(url, 'GET', null, 'json', function (res) {
            const row = $("<div id='roomList' class='row py-2 gy-3 row-cols-4 mx-auto'></div>")
            res.forEach(room => {
                const content = `
                <div class="col">
                    <div class="card mb-2" style="width: 18rem; height: 470px">
                      <img src="/api/img/rooms/${room.id}" class="card-img-top img-fluid h-100" alt="...">
                      <div class="card-body">
                        <p class="card-text"><b>Hotel: </b>${room.hotelName}</p>
                        <p class="card-text"><b>Location: </b>${room.location}</p>
                        <p class="card-text"><b>Price: </b>${room.price}$</p>
                        <p class="card-text"><b>Number of Guests: </b>${room.nrGuests}</p>
                        <button id="${room.id}" class="col-12 btn btn-success bookRoomBtn" hidden="hidden"><i id="${room.id}" class="bi bi-bookmark-check"></i></button>
                      </div>
                    </div>
                </div>
            `;
                row.append(content);
            });
            $('#roomList').remove();
            $('#pageContent').append(row);
            $('#searchForm').removeClass('was-validated');
            registerBookEvent();
            checkIfCanBook();
        });
    }

    function checkIfCanBook() {
        const token = sessionStorage.getItem('token')
        if (token == null)
            return;

        $('.bookRoomBtn').removeAttr('hidden');
    }

    function registerBookEvent() {
        $('.bookRoomBtn').click(function (e) {
            $('#createRequestBtn').attr('href', `/api/requests?roomId=${e.target.id}`);
            $('#checkInModal').text($('#checkInInput').val());
            $('#checkOutModal').text($('#checkOutInput').val());
            $('#bookRoomModal').modal('show');
        });
    }

})(jQuery);