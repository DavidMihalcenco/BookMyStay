(function ($) {

    checkForLogin();
    $(document).ready(function () {
        createErrorModal();
        createAddRoomModal();
        createHotelView();
        registerFormSubmitEvent();
    });

    function registerDeleteEvent() {
        $('#deleteHotel').click(function (event) {
            event.preventDefault();

            makeJsonRequest(this.href, 'DELETE', null, 'text', function () {
               window.location.href = '/hotels';
            }, function (err) {
                displayErrorMessage(err);
                window.location.href = '/hotels';
            });
        });

        $('.deleteRoom').click(function (event) {
            event.preventDefault();

            makeJsonRequest(this.href, 'DELETE', null, 'text', function () {
                window.location.reload();
            }, function (err) {
                displayErrorMessage(err);
            });
        });
    }

    function registerFormSubmitEvent() {
        const url = window.location.href;
        const id = url.substring(url.lastIndexOf('/') + 1);

        $('#roomForm').submit(function(e) {
            const form = $('#roomForm')[0];
            e.preventDefault();

            form.classList.add('was-validated');
            if (!form.checkValidity())
                return

            const formData = new FormData();
            formData.append('img', $('input[type=file]')[0].files[0]);
            formData.append('roomDto', new Blob([JSON.stringify({
                'price': $('#formPriceInput').val(),
                'nrOfGuests': $('#formNrGuestsInput').val(),
            })], {type: 'application/json'}));

            postForm('/api/hotels/' + id + '/rooms', formData, function () {
                window.location.reload();
            } , function (err) {
                $('#addRoomModal').modal('hide');
                displayErrorMessage(err);
            });
        });
    }

    function createAddRoomModal() {
        const content = `
        <div class="modal fade" id="addRoomModal">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2>Add Room</h2>
                        <button id="modalCloseBtn" class="btn-close" data-bs-dismiss="modal" data-bs-target="#addRoomModal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="roomForm" enctype="multipart/form-data" novalidate>
                            <div class="mb-3 form-floating">
                              <input type="number" min="1" max="5000" step="any" class="form-control" placeholder="req" id="formPriceInput" required>
                              <label for="formPriceInput" class="form-label">Room Price (per day)</label>
                            </div>
                            <div class="mb-3 form-floating">
                              <input type="number" min="1" max="30" step="1" class="form-control" placeholder="req" id="formNrGuestsInput" required>
                              <label for="formNrGuestsInput" class="form-label">Number of Guests</label>
                            </div>
                            <div class="mb-3">
                              <label for="formRoomImageInput" class="form-label">Room's photo</label>
                              <input class="form-control" type="file" name="imagefile" placeholder="req" id="formRoomImageInput" required>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-warning" type="submit" id="addRoomBtn" form="roomForm">Add</button>
                    </div>
                </div>
            </div>
        </div>
        `;

        $('#pageContent').append(content);
    }

    function createHotelView() {
        const url = window.location.href;
        const id = url.substring(url.lastIndexOf('/') + 1);
        makeJsonRequest('/api/hotels/' + id, 'GET', null, 'json', function (hotel) {
            const content = `
            <div class="card mb-3 mt-3 overflow-hidden" style="max-height: 220px">
              <div class="row g-0">
                <div class="col-md-2">
                  <img src="/api/img/hotels/${id}" class="img-fluid rounded-start h-100" alt="...">
                </div>
                <div class="col-md-5">
                    <div class="card-body">
                        <h5 class="card-title">Hotel Infromation</h5>
                        <p class="card-text"><b>Name: </b>${hotel.name}</p>
                        <p class="card-text"><b>Location: </b>${hotel.location}</p>
                        <button class="btn btn-warning" data-bs-toggle="modal" data-bs-target="#addRoomModal">Add Room</button>
                    </div>
                </div>
                <div class="col-md-5">
                    <div class="card-body">
                        <h5 class="card-title">Hotel Contact</h5>
                        <p class="card-text"><b>Email: </b>${hotel.ownerEmail}</p>
                        <p class="card-text"><b>Phone number: </b>${hotel.phoneNumber}</p>
                        <a class="btn btn-danger" href="/api/hotels/${id}" role="button" id="deleteHotel">Delete Hotel</a>
                    </div>
                </div>
                </div>
              </div>
            </div>
            `;

            $('#pageContent').append(content);
            createRoomView(hotel);
        }, function (err) {
            displayErrorMessage(err);
            window.location.href = '/hotels';
        });
    };

    function createRoomView(hotel) {
        const url = window.location.href;
        const id = url.substring(url.lastIndexOf('/') + 1);

        const header = `
        <div class="mt-3 mb-1">
            <h3>Rooms</h3>
            <hr>
        </div>
        `;
        $('#pageContent').append(header);

        const row = $("<div class='row py-2 gy-3 row-cols-4 mx-auto'></div>")
        hotel.rooms.forEach(room => {
            const content = `
        <div class="col">
            <div class="card mb-2" style="width: 18rem; height: 400px">
              <img src="/api/img/rooms/${room.id}" class="card-img-top img-fluid h-100" alt="...">
              <div class="card-body">
                <h5 class="card-title">Room Information:</h5>
                <p class="card-text"><b>Price(per day): </b>${room.price}$</p>
                <p class="card-text"><b>Max Number of Guests: </b>${room.nrGuests}</p>
                <div>
                    <a href="/api/hotels/${id}/rooms/${room.id}" class="btn btn-danger deleteRoom">Delete</a>
                </div>
              </div>
            </div>
        </div>
            `;
            row.append(content);
        });

        $('#pageContent').append(row);
        registerDeleteEvent();
    };

})(jQuery);