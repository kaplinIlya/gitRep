<!DOCTYPE HTML>
<html>
<head>
    <title>Test Spring Boot</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<div>${header}</div>
<div>
    <form method = "post" enctype="multipart/form-data">
     <input type = "text" name = "firm" placeholder = "Фирма авто"/>
     <input type = "text" name = "model" placeholder = "Модель авто"/>
     <input type = "text" name = "color" placeholder = "Цвет авто"/>
     <input type = "text" name = "power" placeholder = "Мощность авто"/>
     <input type = "file" name = "file"/>
     <button type = "submit">Добавить</button>
    </form>
</div>
<div>Список авто:</div>
<#list cars as car>
<div>
        <b>${car.id}</b>
        <span>${car.firm}</span>
        <span>${car.model}</span>
        <span>${car.color}</span>
        <span>${car.power} hp</span>
        <div>
            <#if car.filename??>
                <img src = "/img/${car.filename}" height="100" width="100"/>
            </#if>
        </div>
</div>
<#else>
No cars
</#list>
</body>
</html>