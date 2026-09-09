# Assignment I

This project shows the Builder design pattern in Java.

The program creates email messages. It uses one common builder interface, but it can make different results.

`EmailObjectBuilder` creates a real `Email` object.

`EmailPreviewBuilder` creates a text preview of the email.

`EmailDirector` has ready steps for making example emails, like a welcome email and a reminder email.

The `Main` class runs the example and prints the result.
