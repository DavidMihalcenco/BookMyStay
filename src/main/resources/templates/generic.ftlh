<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
    <script src="/scripts/request.js" type="text/javascript"></script>
    <#if scriptSrc??>
        <script src="/scripts/${scriptSrc}" type="text/javascript"></script>
    </#if>
</head>
<body>
<#if token??>
<span id="token" hidden="hidden">${token}</span>
</#if>
<#if refresh??>
<span id="refresh" hidden="hidden">${refresh}</span>
</#if>
<nav class="navbar navbar-expand-lg bg-light">
    <div class="container-fluid">
        <a class="navbar-brand" href="/">BookMyStay</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
            <div class="navbar-nav">
                <a <#if home??>class="nav-link active" aria-current="page"<#else>class="nav-link"</#if> href="/">Home</a>
                <a <#if search??>class="nav-link active" aria-current="page"<#else>class="nav-link"</#if> href="/search">Search</a>
                <a id="nav-bookings" <#if myBookings??>class="nav-link active" aria-current="page"<#else>class="nav-link"</#if> hidden="hidden" href="/myBookings">My Bookings</a>
                <a id="nav-hotels" <#if hotels??>class="nav-link active border-start border-2" aria-current="page"<#else>class="nav-link border-start border-2"</#if> hidden="hidden" href="/hotels">Hotels</a>
                <a id="nav-req" <#if requests??>class="nav-link active" aria-current="page"<#else>class="nav-link"</#if> hidden="hidden" href="/requests">Requests</a>
                <a id="nav-login" class="nav-link" href="/login">Login</a>
                <a id="nav-logout" class="nav-link" hidden="hidden" href="javascript:void(0)">Logout</a>
            </div>
        </div>
    </div>
</nav>
<div id='pageContent' class="container"></div>
</body>
</html>