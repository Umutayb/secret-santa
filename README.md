# Easy Secret Santa 🎅🎁

## Overview

**Secret Santa** is a Java application designed to simplify and automate the process of organizing a Secret Santa gift exchange. It pairs participants, generates personalized emails with recipient details, and sends the emails via SMTP, ensuring a smooth and festive experience for all involved.

![Default Email Template](https://github.com/Umutayb/secret-santa/blob/main/src/main/resources/default-email-template.png?raw=true)

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
3. Maven should be installed on your agent or local machine.
4. **Dependencies**:
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

### Usage

Assume the project is properly set up, Maven is installed and the `participants.json` file and `application.properties` are correctly configured. Run the following commands:

```bash
mvn exec:java -Dexec.mainClass="santa.SecretSantaScript"
```

## Alternatively: How to Use the JSON Editor UI

![JSON Editor](https://github.com/Umutayb/secret-santa/blob/main/src/main/resources/json-editor.png?raw=true)

This section provides a step-by-step guide to using the JSON Editor UI to manage participant data and execute the Secret Santa functionality.

---

### Features Overview
- **View Participants**: Displays the current participants in a table format.
- **Add Participants**: Allows adding new participants to the table.
- **Edit Participants**: Lets you edit participant details directly in the table.
- **Remove Participants**: Deletes selected participants from the table.
- **Load JSON**: Loads participant data from a JSON file.
- **Save JSON**: Saves participant data to a JSON file.
- **Run Secret Santa**: Saves the data and triggers the Secret Santa functionality.

---

### Steps to Use the UI

#### 1. **Loading Default Participants**
- On startup, the UI automatically loads participants from `src/main/resources/participants.json`.
- If the file is missing, a warning is displayed, and you can manually load a JSON file using the "Load JSON" button.

#### 2. **Viewing Participants**
- Participants are displayed in a table with columns: **Name**, **Email**, **Address**, and **Phone Number**.

#### 3. **Adding a Participant**
- Click the **"Add Participant"** button to add a new participant.
- A new row will appear, and the cursor will focus on the **Name** field, prompting you to enter details.
- You must fill in the current row before adding another.

#### 4. **Editing Participant Details**
- Click on any cell in the table to edit the data directly.
- Ensure all required fields are filled out for each participant.

#### 5. **Removing Participants**
- Select the row(s) you want to delete.
- Click the **"Remove Selected"** button to remove the participant(s) from the table.

#### 6. **Loading a JSON File**
- Click the **"Load JSON"** button to load participants from a JSON file.
- Use the file chooser to select a valid JSON file.

#### 7. **Saving to a JSON File**
- Click the **"Save JSON"** button to save the current participant data to a JSON file.
- Use the file chooser to specify the save location.
- The JSON file is saved in a human-readable format.

#### 8. **Running Secret Santa**
- Click the **"Run Secret Santa"** button to:
    1. Save the current participant data to `src/main/resources/participants.json`.
    2. Execute the `SecretSantaScript.main()` method.
- A confirmation message is displayed upon successful execution.

---

### Notes
- **Validation**:
    - Rows with empty fields are not saved or included in the Secret Santa execution.
    - You cannot add a new row until the current row is filled.
- **Default File**:
    - Ensure `src/main/resources/participants.json` exists for the default load and save functionality.
- **Error Handling**:
    - Errors during file operations or script execution are displayed in a popup dialog.

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
