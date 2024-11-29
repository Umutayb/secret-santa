# Secret Santa Script 🎅🎁

## Overview

**Secret Santa Script** is a Java application designed to simplify and automate the process of organizing a Secret Santa gift exchange. It pairs participants, generates personalized emails with recipient details, and sends the emails via SMTP, ensuring a smooth and festive experience for all involved.

---

## Features

- **Automated Matching**: Randomly assigns Secret Santa recipients to each participant without duplicates.
- **Custom Email Templates**: Uses a customizable HTML template to craft festive emails.
- **SMTP Integration**: Sends personalized Secret Santa emails using configured SMTP settings.
- **Dynamic Properties**: Loads configurable properties (e.g., due date, organizer email) from a `.properties` file.
- **Logging**: Provides detailed logging for each step of the process.

---

## Project Structure

- **`santa`**
    - `model.Person`: Represents a participant in the Secret Santa exchange.
    - `model.SecretSanta`: Encapsulates a pairing between a Santa and their recipient.
- **`utils`**
    - `FileUtilities`: Utility methods for file operations, such as reading the email template.
    - `Printer`: Handles application logging.
    - `email.EmailUtilities`: Manages email creation and sending via SMTP.
- **`context.ContextStore`**: Loads and stores application properties for configuration.

---

## Getting Started

### Prerequisites

1. **Java JDK**: Ensure you have Java 8 or above installed (preferably 17).
2. **SMTP Email Account**: Required for sending emails (e.g., Gmail, Outlook).
3. **Dependencies**:
    - Java Mail API (or an equivalent library for email functionality).
    - Include any additional dependencies for property management or file handling.

### Installation

1. Clone the repository:
   ```bash
   git clone http://github.com/Umutayb/secret-santa.git
   cd secret-santa
   ```
2. Add your email and SMTP credentials to `application.properties`:
   ```properties
   host=smtp.emailprovider.com
   sender-email=your-test-email@example.com
   sender-email-application-password=your-email-password
   participants-directory=src/main/resources/participants.json
   organiser-email=your-organiser-email@example.com
   budget=40€
   due-date=2024-12-24
   keep-email-logs=false
   ```

3. Customize the participants in the `participants.json` json within the `src/main/resources/` directory:
   ```json
   {
      "participants": [
        {
          "name": "Mrs. Smith",
          "email": "mrs-smith@email.com",
          "address": "Wonderland!",
          "phoneNumber": "+00000000001"
        },
        {
          "name": "Mr. Smith",
          "email": "smith@email.com",
          "address": "Smithland",
          "phoneNumber": "+00000000000"
        }
      ]
    }
   ```

4. Update the email HTML template (`email.html`) within the (`src/main/java/santa/email/`) directory to reflect your event's branding or additional details.

---

## Usage

1. Compile and run the program:
   ```bash
   javac SecretSantaScript.java
   java santa.SecretSantaScript
   ```
2. The script will:
    - Randomly assign recipients to participants.
    - Personalize the email template with Secret Santa details.
    - Send the emails to participants.
3. Check the logs for status updates and any errors.

---

## Configuration

### Updated `application.properties`

| Property Name                 | Description                                                       | Example Value                          |
|-------------------------------|-------------------------------------------------------------------|----------------------------------------|
| `host`                        | SMTP host address                                                 | `smtp.gmail.com`                       |
| `sender-email`                | Sender's email address                                            | `sender@example.com`                   |
| `sender-email-application-password` | Application password for the email account                        | `examplepassword`                      |
| `participants-directory`      | Path to the participants JSON file                                | `src/main/resources/participants.json` |
| `organiser-email`             | Organizer's email address                                         | `organiser@example.com`                |
| `budget`                      | Suggested budget for gifts                                        | `~40€`                                 |
| `due-date`                    | Deadline for the gift exchange                                    | `2024-12-24`                           |
| `keep-email-logs`             | Toggle for saving email logs locally, keep true for anonimization | `false`                                |

---

## Email Template

The email template (`Email.html`) supports the following placeholders for dynamic content:

| Placeholder            | Description                           |
|------------------------|---------------------------------------|
| `{{Your Name}}`        | Name of the Secret Santa sender       |
| `{{Recipient Name}}`   | Name of the recipient                 |
| `{{Budget}}`           | Gift budget                          |
| `{{Due Date}}`         | Deadline for gift exchange            |
| `{{Organiser Email}}`  | Organizer's contact email             |
| `{{Recipient Address}}`| Recipient's delivery address          |
| `{{Recipient Phone}}`  | Recipient's phone number              |

---

## Contribution

Feel free to fork the project and submit pull requests with new features, bug fixes, or enhancements. Ensure your contributions align with the holiday spirit! 🎄

---

## License

This project is licensed under the [MIT License](LICENSE).

Happy gifting! 🎁