const ordersByDateFilters = () => {
  return `<div class="form-row filter-orders align-items-center">
<form name="filterOrders" class="form-inline" method="get" action="orders">
    <div class="form-check form-check-inline">
        <input type="checkbox" class="form-check-input" id="appointedOrder" name="appointed" value="true">
        <label class="form-check-label" for="appointedOrder">Appointed</label>
    </div>
    <div class="form-check form-check-inline">
        <input type="radio" class="form-check-input" id="filterByToday" name="filterBy" value="today" checked>
        <label class="form-check-label" for="filterByToday">Today</label>
    </div>
    <div class="form-check form-check-inline">
        <input type="radio" class="form-check-input" id="filterByLastWeek" name="filterBy" value="lastWeek">
        <label class="form-check-label" for="filterByLastWeek">Last Week</label>
    </div>
    <div class="input-group input-group-sm">
        <div class="input-group-prepend">
            <div class="input-group-text">
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="radio" id="filterByDate" value="date" name="filterBy">
                  <label class="form-check-label" for="filterByDate">Date</label>
                </div>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="radio" id="filterByWeek" value="week" name="filterBy">
                  <label class="form-check-label" for="filterByWeek">Week</label>
                </div>
            </div>
        </div>
        <div class="input-group-append">
            <input type="date" class="form-control form-control-sm" id="filterByDate" name="date">
        </div>
    </div>
    <button type="submit" class="ml-2 filter-order btn btn-sm btn-secondary"><span data-feather="arrow-right"></span></button>
</form>`;
};

document.getElementById("ordersByDate").addEventListener("click", (e) => {
    e.preventDefault();
    const el = e.target;

    putContent(el.dataset.title, "", ordersByDateFilters());

    // document.querySelectorAll("a.filter-order").forEach(link => {
    //     link.addEventListener("click", e => {
    //         e.preventDefault();
    //         ajaxRequest(link.href).then(response => {
    //             const table = createTable("orders-list", getOrdersColumns(), response, orderRow, true, "orders");
    //             putContent(el.dataset.title, table, "");
    //         });
    //     });
    // });

    document.forms.namedItem("filterOrders").addEventListener("submit", e => {
        e.preventDefault();
        ajaxRequest(e.target.action, "get", getFormData("filterOrders")).then(response => {
            const table = createTable("orders-list", getOrdersColumns(), response, orderRow, true, "orders");
            putContent(el.dataset.title, table, "");
        });
    });
});
