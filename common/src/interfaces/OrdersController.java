package interfaces;

import entities.Entity;
import entities.Order;
import exceptions.InvalidInputException;
import exceptions.RecordAlreadyExistsException;
import exceptions.RecordNotFoundException;
import wrappers.OrderWrapper;
import wrappers.Wrapper;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

public interface OrdersController extends Controller {

    @Override
    OrderWrapper get(int id) throws RecordNotFoundException;

    @Override
    Integer add(Wrapper order) throws RecordAlreadyExistsException, InvalidInputException, RecordNotFoundException;

    Integer merge(OrderWrapper orderToKeep, OrderWrapper orderToMerge) throws RecordAlreadyExistsException, InvalidInputException, RecordNotFoundException;

    boolean checkOrdersSameTimePeriod(OrderWrapper orderToKeep, OrderWrapper orderToMerge);

    boolean checkOrdersHaveSameBoatType(OrderWrapper orderToKeep, OrderWrapper orderToMerge);

    boolean orderHaveSpaceForMerge(OrderWrapper orderToKeep, OrderWrapper orderToMerge);

    @Override
    void update(int id, Wrapper order) throws RecordAlreadyExistsException, RecordNotFoundException, InvalidInputException;

    @Override
    void delete(int id) throws RecordNotFoundException;

    @Override
    Order[] getList();

    @Override
    Order[] filterList(Predicate<? super Entity> condition);

    @Override
    Order getRecord(int id) throws RecordNotFoundException;

    void duplicateOrder(int orderId, OrderWrapper duplicated) throws RecordNotFoundException, CloneNotSupportedException, InvalidInputException;

    /**
     * Find orders of a rower based on approved/not approved field
     *
     * @param rowerId the rower id
     * @return orders
     */
    Order[] findOrdersByRower(int rowerId, boolean approvedRequests);

    /**
     * Find all orders of a rower
     *
     * @param rowerId the rower id
     * @return orders
     */
    Order[] findOrdersByRower(int rowerId);

    List<Integer> findOrderRowers(Order order);

    /**
     * Find orders by activity date
     *
     * @param date activity date
     * @return orders
     */
    Order[] findOrdersByDate(LocalDate date);

    /**
     * Find orders by activity date
     *
     * @param date activity date
     * @return orders
     */
    Order[] findOrdersByDateOfRower(int rowerId, LocalDate date);

    /**
     * Find approved orders of specific rower by activity date
     *
     * @param date activity date
     * @return orders
     */
    Order[] findApprovedOrdersByDateOfRower(int rowerId, LocalDate date);

    Order[] findOrdersFromLastWeek();

    Order[] findOrdersFromDateToDate(LocalDate fromDate, LocalDate afterDate);

    Order[] findOrdersFromDateToDateOfRower(int rowerId, LocalDate finalFromDate, LocalDate finalAfterDate);

    /**
     * Find not-yes approved orders with a specific boat
     *
     * @param boatId boat id
     * @return orders
     */
    Order[] findOrdersByBoat(int boatId, boolean approvedRequest);

    void deleteOrdersWithBoat(int boatId);

    void addRowerToOrder(int rowerId, int orderId) throws RecordNotFoundException, InvalidInputException;

    void deleteRowerFromOrder(int rowerId, int orderId) throws RecordNotFoundException;

    void appointBoatToOrder(int boatId, int orderId) throws RecordNotFoundException, InvalidInputException;

    /**
     * Validate all the things before inserting/updating an order
     *
     * @param order order wrapper before updating in database
     * @throws InvalidInputException when error occurred
     */
    void validateOrder(OrderWrapper order) throws InvalidInputException;

    /**
     * Check if a rower has overlapping orders based on specific order
     *
     * @param rowerId rower id
     * @param orderId order id which the rower is registered
     * @throws InvalidInputException overlapping orders have found
     */
    void checkRowerSameOrder(int rowerId, int orderId) throws InvalidInputException;

    /**
     * Check if a boat has overlapping orders based on specific order
     *
     * @param boatId  boat id
     * @param orderId order is which the boat is registered
     * @throws InvalidInputException overlapping orders have found
     */
    void checkBoatSameOrder(int boatId, int orderId) throws InvalidInputException;

    void checkBoatTypeCapacity(int maxCapacityBoat, List<Integer> rowerIds) throws InvalidInputException;

    void checkRowersCapacityForBoat(int maxCapacityBoat, List<Integer> rowerIds) throws InvalidInputException;

    void checkBoatCapacityForOrder(int maxCapacityBoat, List<Integer> rowerIds) throws InvalidInputException;

    boolean checkOrdersOverlappingTimes(int orderId, int orderToCheckId);
}
