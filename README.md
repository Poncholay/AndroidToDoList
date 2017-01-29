AndroidToDoList
=======

![alt tag](https://raw.githubusercontent.com/Poncholay/AndroidToDoList/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

A simple To do list app

## 1. Technical specifications

ToDoList is an android application compatible with the Android SDK API 19.
It is a gradle based project so one must use gradle to build it.

It is made of two activities. The first one contains a FragmentViewPager which is populated with two fragments.

This projects uses numerous libraries. Here is the list :
AwesomeSplash : Used for the splash animation.
ParallaxViewPager : Adds a parallax effect to the ViewPager.
MaterialDialogs : Provides simple clean dialogs.
Boommenu : Used for the sorting interface, provide a clean animated popup menu.
TextDrawable : Used for the sorting interface, creates drawables from text.
QMBForm : Provide modular forms.

And for the tests :
Junit
Powermock / Mockito
Robolectric
Espresso

## 2. Specifications

The app allows one to keep track of tasks by simply logging them into the application.

A task must contain a title which should be as concise as possible as it will be the first thing the user will see in the list. The title cannot be longer than 30 characters. This allows a cleaner experience.
A task can contain a content, it is a description of the task and can be as long as one hundred characters. The content provides details on the task.
Finally, a task can contain a date. This date is a deadline for the realisation of the task. It can also be helpful to sort the tasks.

When a task is added, it is stored in a database so that it persists from launch to launch.
It is then appended to the task list which displays unfinished tasks. There also exists another list that displays completed tasks.
The tasks can be switched back and forth between those two lists and they can also be completely removed from the app and database if needed.

There is a built in sorting system which allows the user to sort his tasks four different ways. The selected configuration is then stored in the app preferences so that it is retained when the app is relaunched. The user can sort by date or alphabetically, and can reverse the order.

## 3. User interface

![alt tag](https://raw.githubusercontent.com/Poncholay/AndroidToDoList/master/README/ui.png)

The user interface is pretty straight forward. It is made of two activities.
The first activity is the listing activity. It contains a FragmentViewPager which allows to swipe between the two lists. It also contains a menu bar with an button that takes the user to the other activity to create a new task. There is also a floating button which when tapped, reveals the four sorting options.
The task lists are of course scrollable. When the user taps a task, he is taken to the second activity to edit the task. When he long taps a task, a dialog pops up and allows the task to be completed (or uncompleted depending on it’s current state) or deleted.

The second activity allows the user to create or edit tasks via a simple form. The title and content are written in textViews and the date is chosen with a datePicker.
This activity is also used to consult a task and show it in it’s entirety.
The activity has a menubar with a back arrow to go back to the list without saving and a save button if changes have been made to the form, which sends the user back to the list along with the task.
