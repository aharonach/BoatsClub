# BoatsClub

## Owners:

Aharon Achildiev

Joad Hamdan

## How to run the program

The database should be in tomcat&#39;s directory -\&gt; webapps -\&gt; boatsclub-resources.

Put the war file in tomcat&#39;s webapps directory and start tomcat.

Open chrome and go to: [http://localhost:8080/boatsclub](http://localhost:8080/boatsclub)

The login page should appear automatically.

You can access the application in [http://34.228.65.247:8080/boatsclub/](http://54.165.228.98:8080/boatsclub/) (Bonus).

## Login details

**Admin user:**

Email: [admin@boatsclub.com](mailto:admin@boatsclub.com)

Password: boatsclub

**Regular user:**

Email: non-admin@boatsclub.com

Password: boatsclub

## Introduction

A web application for Boats Club Management System.

### Java servlets with tomcat

Single page application (without routing or states support) using Java 8 + Servelts + HTML, CSS and Vanilla JavaScript.

### System features

The system has a login/logout mechanism.

Four main lists are managed: rowers, boats, activities, and orders (rower requests).

Each rower is a club member, and he can create a new order for the manager and ask for a boat to row with – by himself or with other club rowers.

The manager can view awaiting orders and appoint boats to each order.

The manager has permission to edit each record in the system.

The manager can import and export: boats, rowers, and activities using XML files. Export files saved at the root path.

The system saves its state after every operation to the records lists.

(NEW) The admin can send global messages to all users. The messages appear at the dashboard/main page of the application.

(NEW) When admin or user changes an order, an automatic notification is sent to all related rowers. The notifications panel appear at the top bar of the app, after clicking on the bell icon.

**Module: &quot;Engine&quot;**

The backend of the system. It handles all the logic and final validations of the user and database (state XML and record lists) access and manipulations.

The Engine class initializes the system, the databases, and the controllers.

**Module: &quot;Common&quot;**

The module includes all classes which are used both from the Engine and Server modules: Entities (Rower, Boat, Order, Activity) as well as interfaces for the engine and each controller, exceptions, utilities, and encryption.

Each record (Rower, Boat, Activity, Order) extends an abstract class called &quot;Entity&quot; to represent a system&#39;s data member. Each has a unique ID, which the system can refer to.

Also, each record has a &quot;Controller&quot; class (implements an interface), which handles all the operations that come from the user (from the &quot;Console&quot; module).

**(NEW) Module: &quot;Web&quot;**

This web module contains the servlets and front-end assets such as HTML, CSS, and JavaScript files.

We have 4 main servlets responsible for Boats, Rowers, Orders, Activities, as well as Login, Import &amp; Export. The Main servlet redirects the user to the proper page based on his role (admin or regular user).

Each main servlet has 4 main URLs and methods, for more easy access to each action: getting a list/record, edit, add, delete. The Orders servlet has more actions such as appoint, duplicate, and merge.

Each servlet sends a response object as Json (with GSON library). It can be a &quot;Response&quot; class object with response status, a simple record, or a list of records.

JavaScript is playing a big role here – it&#39;s responsible for displaying content on the webpage when the user clicks on any menu item or link.

We have JS files for the admin and the user, and we have common JS files that are used for both.

## Bonus

### Responsive UI:

The web application is responsive and usable in all mobile phones and tablets.

### Deployment to AWS

The application uploaded to AWS server with Java 8 and Tomcat 8. It can be accessed from [http://54.165.228.98:8080/boatsclub/](http://54.165.228.98:8080/boatsclub/).

## Notes

**Important:** Schemes and Database (State) XML files are saved and should be in &quot;boatsclub-resources&quot; inside tomcat directory -\&gt; webapps.

### Notifications

1. Notifications are sent to all rowers related (old or new) after editing an order.
2. They are sent also when a rower edits his order – we think it&#39;s a better option because everyone needs to know about their changed order.

### Managing an order and activity:

1. For other operations on orders, such as: adding/removing rowers, change activity date and times, the manager should go to the &quot;edit order&quot; menu item.
2. The system saves an array of rowers&#39; ids and the boat id for the appointed order on every order.
3. When deleting boats, all order appointed with it will be deleted as well.
4. When merging two orders (appoint order option) – these both should be at the same activity time, same boat type, and the exact boat types requirements. If there is more than one boat type in each order, we will merge the orders based on the bigger boat.
5. The rower can only edit open orders. Already approved orders are not allowed to be edited.
6. The activity time frame can be very long (For ex: 05:00 until 23:00). We don&#39;t have a limit for that.

### XML Database &amp; Import/Export:

1. The XSD schemes included in &quot;engine.jar&quot; and &quot;webapp.war&quot;, instead of inside the resources directory, to support functionality in a real UNIX server.
2. &quot;Has Coxswain&quot; field in boats is ignored when importing because it&#39;s informed inside the boat type.
3. Importing activities: can only add new activities because the import file doesn&#39;t have an ID.

### Managing rowers:

1. Admins can remove their admin permissions.
2. When there is only one rower (manager), and the manager deleted himself from the club, the system continues to operate until he exits the system.
