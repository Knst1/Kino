<#import "parts/common.ftlh" as c>

<@c.page title="Аренды">
    <h5>Аренды</h5>
    <ul class="nav nav-pills justify-content-center">
        <li class="nav-item">
            <a class="nav-link" href="/allrents?page=1&size=21">Все</a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/prerents?page=1&size=21">Неподтверждённые</a>
        </li>
        <li class="nav-item">
            <a class="nav-link active" href="">Подтверждённые</a>
        </li>
    </ul>
    <div class="card-columns">
        <#list rents as rent>
            <div class="card border-success my-3">
                <div class="m-2">
                     <#if rent.movieAccess>
                    <a href="/movie/${rent.movie.id}" style="color: #000000; ">
                        <span><b>${rent.movie.name}</b></span>
                    </a>
                    <i> (${rent.movie.year})</i>
                    <#else>
                        <span style="color: #550000; ">На данный момент название фильма недоступно</span>
                     </#if>
                    <p># ${rent.id}</p>

                </div>
                <div class="card-footer text-success">
                    <p>Продолжительность аренды: ${rent.duration} д.</p>
                    <p>Стоимость аренды: $ ${rent.price}</p>
                </div>
            </div>
        <#else>
            <div class="container mt-5">
                Список пуст
            </div>
        </#list>
    </div>
    <#if realsize != 0>
        <div class="container mt-5"/>
        <#assign path="/rents">
        <#include "parts/pagination.ftlh">
    </#if>
</@c.page>