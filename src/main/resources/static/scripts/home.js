(function ($) {

    $(document).ready(function () {
        createHomePage();
    });

    function createHomePage() {
        $('#pageContent').addClass('container-fluid');
        $('#pageContent').removeClass('container')

        const carousel = `
        <div id="carousel-home-page" class="carousel slide" data-bs-ride="carousel">
            <div class="carousel-inner">
                <div class="carousel-item">
                    <img src="/img/homePage/slide1.jpg" class="d-block w-100 object-fit-cover" style="height: calc(100vh - 56px);" alt="...">
                    <div class="carousel-caption top-0 mt-5 d-none d-md-block">
                        <h1 class="display-1 mt-5 fw-bold">Stay in comfort, book with ease</h1>
                        <a class="btn btn-warning btn-lg px-5 py-3 mt-4 text-white" href="/search" role="button">
                            Book a room
                        </a>
                    </div>
                </div>
                <div class="carousel-item active">
                    <img src="/img/homePage/slide2.jpg" class="d-block w-100 object-fit-cover" style="height: calc(100vh - 56px);" alt="...">
                    <div class="carousel-caption top-0 d-none d-md-block">
                        <h1 class="display-1 mt-5 fw-bold">Wherever you go, we've got a room to show</h1>
                        <a class="btn btn-warning btn-lg px-5 py-3 mt-4 text-white" href="/search" role="button">
                            Book a room
                        </a>
                    </div>
                </div>
                <div class="carousel-item">
                    <img src="/img/homePage/slide3.jpg" class="d-block w-100 object-fit-cover" style="height: calc(100vh - 56px);" alt="...">
                    <div class="carousel-caption top-0 d-none d-md-block">
                        <h1 class="display-1 mt-5 fw-bold">Book your stay, unlock your adventure</h1>
                        <a class="btn btn-warning btn-lg px-5 py-3 mt-4 text-white" href="/search" role="button">
                            Book a room
                        </a>
                    </div>
                </div>
            </div>
            <button class="carousel-control-prev" type="button" data-bs-target="#carousel-home-page" data-bs-slide="prev">
                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Previous</span>
            </button>
            <button class="carousel-control-next" type="button" data-bs-target="#carousel-home-page" data-bs-slide="next">
                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Next</span>
            </button>
        </div>
        `

        $('#pageContent').append(carousel);
    }

})(jQuery);