const userAjaxUrl = "ratings/votes";
let restId;

$.ajaxSetup({
    beforeSend: function (xhr) {
        xhr.setRequestHeader('X-CSRF-TOKEN', $("meta[name='_csrf']").attr("content"));
    }
});

$(".newvote").click(function () {
    console.log("newVoteId: " + $(this).parent().parent().find('.restaurantId').text());
    restId = $(this).parent().parent().find('.restaurantId').text();
    //  https://stackoverflow.com/a/22213543/548473
    $.ajax({
        url: userAjaxUrl,
        type: "POST",
        data: JSON.stringify({
            restaurantId: restId
        }),
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            openPopup("Your vote accepted", false);
        },
        error: function (error) {
            if (error.responseJSON != null && error.responseJSON.message === "You have voted today already. Use PUT to change your mind") {
                openPopup("You have already voted today", true);
            } else if (error.responseJSON != null && error.status == 422) {
                console.log("Something went wrong", error);
                openPopup(error.responseJSON.message, false);
            } else {
                console.log("Something went wrong", error);
                openPopup(error.responseText, false);
            }
        }
    });
});

$(".revote, .popup").click(function () {
    console.log("reVoteId: " + restId);
    $.ajax({
        url: userAjaxUrl,
        type: "PUT",
        data: JSON.stringify({
            restaurantId: restId
        }),
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            openPopup("Your vote has been changed", false);
        },
        error: function (error) {
            console.log("Something went wrong", error);
            openPopup(error.responseJSON.message, false);
        }
    });
});

function openPopup(warnText, revoteFlg){
    $(".warnText").text(warnText);
    $(".popup, .popup-content").addClass("active");
    $(".content").addClass("hidden");
    if(revoteFlg){
        $(".revote").removeClass("hidden");
    } else{
        $(".revote").addClass("hidden");
    }
}

$(".close, .popup").click(function () {
    $(".popup, .popup-content").removeClass("active");
    $(".content").removeClass("hidden");
    window.location.reload();
});