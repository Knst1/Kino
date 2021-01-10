<#import "parts/common.ftlh" as c>

<@c.page title=movie.name style="../style.css">
    <h5>${movie.name} (${movie.year})</h5>
    <div class="container emp-profile">
        <div class="col-md-8">
            <div class="tab-content movie-tab" id="myTabContent">
                <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                    <div class="row">
                        <div class="col-md-6">
                            <label>Название</label>
                        </div>
                        <div class="col-md-6">
                            <p>${movie.name}</p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <label>Год выхода</label>
                        </div>
                        <div class="col-md-6">
                            <p>${movie.year}</p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <label>Продюсер</label>
                        </div>
                        <div class="col-md-6">
                            <p>${movie.producer}</p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <label>IMDB</label>
                        </div>
                        <div class="col-md-6">
                            <p>${movie.imdb}</p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <label>Стоимость суточной аренды</label>
                        </div>
                        <div class="col-md-6">
                            <p>$ ${movie.cost}</p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <label>Количество дней аренды</label>
                        </div>
                        <div class="col-md-6">
                            <p>${movie.count}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <#if rent>
        <#if ok>
            <div class="alert alert-success" role="alert">Аренда успешно оформлена. Срок в днях: ${duration}. Подтвердить её можно
                <a href="/prerents?page=1&size=21">здесь</a>.</div>
        <#else>
            <div class="alert alert-danger" role="alert">Не удалось оформить аренду. Попробуйте позже.</div>
        </#if>
    </#if>
    <form action="/rent" method="post">
        <div class="form-row align-items-center">
        <input type="hidden" name="movieId" value="${movie.id}"/>
        <div class="col-auto">
            <label for="inputDays">Срок аренды в днях</label>
            <input type="number" name="duration" min="1" max="1000" value="1" class="form-control" id="inputDays" aria-describedby="days">
            <small id="days" class="form-text text-muted">Количество дней, на которое предоставляется право показа фильма</small>
        </div>
        <div class="col-auto">
        <button type="submit" class="btn btn-primary">Оформить</button>
        </div>
        </div>
    </form>
</@c.page>
