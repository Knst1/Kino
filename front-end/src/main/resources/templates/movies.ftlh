<#import "parts/common.ftlh" as c>

<@c.page title="Фильмы">
    <h5>Список фильмов</h5>
    <div class="card-columns">
        <#list movies as movie>
            <div class="card my-3">
                <div class="m-2">
                    <a href="/movie/${movie.id}" style="color: #000000; ">
                        <span><b>${movie.name}</b></span>
                    </a>
                    <i> (${movie.year})</i>
                </div>
                <div class="card-footer text-muted">Дней аренды: ${movie.count}</div>
            </div>
        <#else>
    <div class="container mt-5">
            Список пуст
    </div>
        </#list>
    </div>
    <#if realsize != 0>
    <div class="container mt-5"/>
        <#assign path="/movies">
        <#include "parts/pagination.ftlh">
    </#if>
</@c.page>