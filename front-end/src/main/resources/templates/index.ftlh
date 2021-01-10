<#import "parts/common.ftlh" as c>

<@c.page title=cinema.name>
    <h5>Профиль кинотеатра</h5>
    <div class="container emp-profile">
        <div class="col-md-8">
            <div class="tab-content profile-tab" id="myTabContent">
                <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                    <div class="row">
                        <div class="col-md-6">
                            <label>Название</label>
                        </div>
                        <div class="col-md-6">
                            <p>${cinema.name}</p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <label>Директор</label>
                        </div>
                        <div class="col-md-6">
                            <p>${cinema.chief}</p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <label>Владелец</label>
                        </div>
                        <div class="col-md-6">
                            <p>${cinema.owner}</p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <label>Телефон</label>
                        </div>
                        <div class="col-md-6">
                            <p>${cinema.phone}</p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <label>Адрес</label>
                        </div>
                        <div class="col-md-6">
                            <p>${cinema.address}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@c.page>