const ordersByDateFilters = () => {
  return `<div class="form-row filter-orders align-items-center">
    <div class="col-auto">
      <a class="btn btn-secondary btn-sm filter-order" href="orders?filterBy=today">Today</a>
    </div>
    <div class="col-auto">
      <a class="btn btn-secondary btn-sm filter-order" href="orders?filterBy=lastWeek">Last Week</a>
    </div>
    <div class="col-auto">
        <form action="orders" name="filterByDate" method="get">
            <div class="input-group input-group-sm">
                <div class="input-group-prepend">
                    <div class="input-group-text">
                        <div class="form-check form-check-inline">
                          <input class="form-check-input" type="radio" id="filterByDate" value="date" name="filterBy" checked>
                          <label class="form-check-label" for="filterByDate">Date</label>
                        </div>
                        <div class="form-check form-check-inline">
                          <input class="form-check-input" type="radio" id="filterByWeek" value="week" name="filterBy">
                          <label class="form-check-label" for="filterByWeek">Week</label>
                        </div>
                    </div>
                </div>
                <input type="date" class="form-control form-control-sm" id="filterByDate" name="date">
                <div class="input-group-append">
                    <button type="submit" class="filter-order btn btn-sm btn-secondary"><span data-feather="arrow-right"></span></button>
                </div>
            </div>
        </form>
    </div>
  </div>`;
};

document.getElementById('ordersByDate').addEventListener('click', (e) => {
    e.preventDefault();
    const el = e.target;

    putContent(el.text, "", ordersByDateFilters());

    document.querySelectorAll('a.filter-order').forEach(link => {
        link.addEventListener("click", e => {
            e.preventDefault();
            ajaxRequest(link.href).then(response => {
                const table = createTable('orders-list', getOrdersColumns(), response, orderRow, true, "orders");
                putContent(el.text, table, "");
            });
        });
    });

    document.forms.namedItem("filterByDate").addEventListener("submit", e => {
        e.preventDefault();
        console.log(getFormData("filterByDate").toString());
        ajaxRequest(e.target.action, 'get', getFormData("filterByDate")).then(response => {
            const table = createTable('orders-list', getOrdersColumns(), response, orderRow, true, "orders");
            putContent(el.dataset.title, table, "");
        });
    });
});
