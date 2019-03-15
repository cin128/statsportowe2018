$(document).ready(function() {
    $("#locales").change(function () {
        var selectedOption = $('#locales').val();
        if (selectedOption != ''){
            var hrefval =window.location.href;
            if(hrefval.search('\\?')>0) {
                if (hrefval.search('lang=')>0) {
                    var url = hrefval.split('lang=')[0] + "lang=" + selectedOption;
                } else {
                    var url = hrefval + "&lang=" + selectedOption;
                }
            }else{
                var url = hrefval+"?lang="+selectedOption;
            }
            window.location.replace(url);
        }
    });
});
