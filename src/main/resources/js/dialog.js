//Переназначаем событие click по ссылке, чтобы открывалось окно диалога
(function ($) {
   $(function () { 
            //отвязываем событие от пункта меню (web-item с ключом pages-menu)
            AJS.$('#pages-menu').unbind('click');
            //назначаем новое
            AJS.$('#pages-menu').bind('click', function (e) {
            //отменяем действие по умолчанию
            e.preventDefault();
            
            //назначаем новое действие
            var link = AJS.$(this);
            AJS.$.get(link.attr('href'), function (response) {
                //встраиваем на страницу код ответа сервера
                AJS.$('.aui-nav-imagelink').after(response);
                //показываем окно диалога
                AJS.dialog2("#pages").show();                
            });
            return false;
        });
    });
             
})(AJS.$);