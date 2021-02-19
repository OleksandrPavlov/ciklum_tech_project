Configuring app.
	1. You have to adjust your DB the application will interact with.
	To make that, please go to /ciklum/local.properties file. There you have to 
	override basic JDBC configurations for you custom needs.
		Example:
			db.driver=com.mysql.cj.jdbc.Driver
			db.url=jdbc:mysql://localhost:3306/ciklum_app?serverTimezone=UTC&characterEncoding=utf8
			db.username=root
			db.password=12345
			db.poolSize=10
			db.maxPoolSize=20 
	
	2. As ciklum app supposes interaction console - user, it strongly recommended to override 
	console encoding parameter in /ciklum/local.properties to correct representation non ASCII symbols.
		Example: 
			console.charset=UTF-8
	3. To build schema in your DB you have to execute ciklum_app_mysql_script_2021 script placed in this directory.
Launching app.
	1. At the root directory where this file located, invoke command line and execute 
	"java -jar ciklum.jar" command without quotes. If you see hieroglyphs insteed latters, please jump
	to Configurating section of this manual.
Launching tests.
	To run the test you have to invoke command line in the root directory(the folder where this fiel placed).
	The run the command: "java -jar ciklum-tests.jar" without quotes. After that test_result.txt file will appear and will contain 
		test executing information.
Logging: During application lifecycle, the app writes logs into app_execution.log file.





User Interface.
	 App flow devided on three part:
		1. Choosing language.
		2. Authentication.
		3. Main menu.
Flows:
	1. Choosing language.
		User have to choose language of interface(currently supported - english, russian).
		Language choosing only one at the beginning, and later it will impossible to return to this action.
	2. Authentication.
		This step devided on two option:
			1. SignIn - this option afford users that already have account in this app to 
				sign in to the system  by credentials entering LOGIN and PASSWORD.
			2. Registration - user prompted to enter USERNAME and PASSWORD. After that
			he will be	entered to the system and later will be able to enter by SignIn option after reloading app.
	3.Mein Menu.
		This step provide user different option for managing products and orders.
		The menu consists of:
			1.Show all products.
				This option displays all products that is in DB.
			2.Show all ordered products.
			This option will display all products that currently bound to orders across entire system.
			3.Create new product.
				This option creates new product in DB. To add new product user have to enter: product name,
				product price and product status. After that user may to observe newly created product in "Show all products" option.
			4.Show current order.
				This option allows to look at order. The order contains ordered products that was added by "Add products to my order"
				command.
			5.Update current order.
				A command provides user posability of changing product quantity of order. To change the product quantity 
				user have to enter product identifier of intrested product and than new quantity. But if order does not have such id,
				error notification will be thrown.
			6.Add product to my order.
				This feature appends specified by identifier product to the current order. If no one product by specified id, 
				a warning notification will be shown.
			7.Remove product.
				Removes product by specified ID, if there are not such products, an worning notification massege will be thrown.
			8.Remove all products.
				This command removes all products from DB, but to apply this action user have to enter authentication password first.
			0.Exit
			This option just stop the app.
				
!!! After script has been executed , there already will account by credentials : LOGIN - adminn, PASSWORD - Admin1@@

	