(function ($) {

    checkForLogin();
    $(document).ready(function () {
        makeJsonRequest("/api/hotels", "GET", null, 'json', function (response) {
           createAddButton();
           createCards(response);
        }, null);
    });

    function createAddButton(){
        const content = `
        <div class="row justify-content-end py-2"> 
            <a class="col-2 btn btn-warning" href="/hotels/new" role="button">Add hotel</a>
        </div>
        `
        $('.container').append(content);
    }

    function createCards(cards) {
        const row = $("<div class='row py-2 gy-3 row-cols-3'></div>")

        cards.forEach(card => {
            const content = `
        <div class="col">
            <div class="card mb-3 overflow-hidden" style="height: 142px">
                <div class="row g-0">
                    <div class="col-md-4">
                        <img src="/api/img/hotels/${card.id}" class="img-fluid rounded-start h-100" alt="...">
                    </div>
                    <div class="col-md-8">
                        <div class="card-body">
                            <h5 class="card-title">${card.name}</h5>
                            <p class="card-text"><b>Location: </b>${card.location}</p>
                            <a class="btn btn-warning" href="/hotels/${card.id}" role="button">More</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
            `
            row.append(content);
        });
        $('.container').append(row);
    }

})(jQuery);