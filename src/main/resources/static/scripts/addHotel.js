(function ($) {

    checkForLogin();
    $(document).ready(function () {
        createErrorModal();
        createForm();
        registerFormSubmitEvent();
    });

    function registerFormSubmitEvent() {

        $('#hotelForm').submit(function(e) {
            const form = $('#hotelForm')[0];
            e.preventDefault();

            form.classList.add('was-validated');
            if (!form.checkValidity())
                return

            const formData = new FormData();
            formData.append('img', $('input[type=file]')[0].files[0]);
            formData.append('hotelDto', new Blob([JSON.stringify({
                'name': $('#formNameInput').val(),
                'location': $('#formLocationInput').val(),
                'phoneNumber': $('#formTelInput').val(),
            })], {type: 'application/json'}));

            postForm('/api/hotels', formData, function () {
               window.location.href = '/hotels';
            }, function (err) {
                displayErrorMessage(err);
            });
        });
    }

    function createForm() {
        const content = `
        <form id="hotelForm" enctype="multipart/form-data" novalidate>
            <div class="col-6 card mt-4 mx-auto">
              <div class="card-header">
                Add new hotel
              </div>
              <div class="card-body">
                <div class="mb-3 form-floating">
                  <input type="text" class="form-control" placeholder="req" id="formNameInput" maxlength="70" minlength="5" required>
                  <label for="formNameInput" class="form-label">Hotel's name</label>
                </div>
                <div class="mb-3 form-floating">
                  <input type="text" class="form-control" placeholder="req" id="formLocationInput" minlength="5" maxlength="50" required>
                  <label for="formLocationInput" class="form-label">Location</label>
                </div>
                <div class="mb-3 form-floating">
                  <input type="tel" class="form-control" id="formTelInput" placeholder="req" minlength="8" maxlength="16" required>
                  <label for="formTelInput" class="form-label">Phone Number</label>
                </div>
                <div class="mb-3">
                  <label for="formHotelImageInput" class="form-label">Hotel's photo</label>
                  <input class="form-control" type="file" name="imagefile" placeholder="req" id="formHotelImageInput" required>
                </div>
                <div class="col-12">
                  <button type="submit" id="addHotelButton" class="btn btn-warning">Add</button>
                </div>
              </div>
            </div>
        </form>
        `

        $('#pageContent').append(content);
    }

})(jQuery);