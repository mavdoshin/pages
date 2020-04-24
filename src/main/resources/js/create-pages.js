/*клиентская часть функционала по созданию страниц*/
/*показывает форму отчета и отправляет запрос с введенными пользователем данными на сервер*/
var SpaceKey;
var FileData;
var File;

(function ($) {
    AJS.toInit(function () {
        
        //получаем выбранный пользователем файл и данные о страницах/метках из него
        AJS.$("#file-select").change(function () {
            //путь к выбранному файлу
            File = AJS.$(this).val();
            //данные о страницах/метках
            FileData='';
            var ext;

            //выполняем, если выбран файл
            if (File!=="")
                {
                    //селектор расширений файла
                    ext = AJS.$(this).val().match(/\.([^\.]+)$/)[1];

                    //проверка типа файла
                    if(ext==='xlsx')
                        {
                            //файл, выбранный пользователем    
                            var selected_file = (AJS.$(this))[0].files[0];

                            //парсер .xlsx файла               
                            readXlsxFile(selected_file).then(function(data) {
                                    // `data` - строковый массив, в каждом элементе строка таблицы из файла
                                    FileData=data;
                                //если не удалось прочитать файл
                                }, function (error) {
                                        console.error(error);
                                        alert("Error! Select the correct .xlsx file.");

                                    });

                        }
                    else
                        {
                            alert('Error! Select .xlsx file.');
                            //очищаем поле выбора файла
                            FileData='';
                            this.value='';
                        }
                }

           })
        .change();

        //получаем выбранное пользователем пространство
        AJS.$("#spaces").change(function () {
           //console.log(AJS.$(this).val());
           SpaceKey = AJS.$(this).val();
        })
        .change();

        AJS.$('#dialog-form').on('click','#dialog-button', function(e) {
            //отменяем действие по умолчанию для клика по кнопке
            e.preventDefault();
            //получаем ссылку для перехода из формы
            var link = AJS.$('#dialog-form').attr('action');

            //console.log(AJS.$("select option:selected:last").text());            
            //console.log(encodeURI(link+"?spaces="+SpaceKey+"&file="+File+"&filedata="+FileData));
            //добавляем к ссылке параметры и переходим. открываем форму отчета.
            AJS.$.get(encodeURI(link+"?spaces="+SpaceKey+"&file="+File+"&filedata="+FileData), function (response) {
                //встраиваем на страницу код ответа сервера
                AJS.$('.aui-nav-imagelink').after(response);
                //показываем окно диалога
                AJS.dialog2("#pages1").show();
            });
        });
    });
})(AJS.$);
