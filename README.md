# sc2002_OOPAssignment_HospitalManagementSystem

## THE ASSIGNMENT 
The assignment for your group will be to design and develop a: 
Hospital Management System (HMS) 
HMS is an application aimed at automating the management of hospital operations, 
including patient management, appointment scheduling, staff management, and billing. 
The system is expected to facilitate efficient management of hospital resources, enhance 
patient care, and streamline administrative processes.

+ CSV file can be found [here](https://github.com/zhihaolaw/sc2002_OOPAssignment_HospitalManagementSystem/tree/main/Patient_Management_system/data)
+ YouTube Link to test cases [here](https://www.youtube.com/playlist?list=PLE1gXAO1j9phnYgYi6XZs5tpKHlPeWA6p)

## User Roles and System Capabilities: 

## 1. All Users: 
+ Users must log in to the system using their unique hospital ID and a default 
password ("password"). 
+ The system should validate login credentials and provide role-specific access to 
the HMS. 
+ Users can change their password after their initial login. 
+ Users will have roles such as Patient, Doctor, Pharmacist or Administrator.

## 2. Patient:
+ Information Access: 
○ Patients can view their own medical record, which consists of: 
+ Patient ID, Name, Date of Birth, Gender 
+ Contact Information (e.g., phone number, email address) 
+ Blood Type 
+ Past Diagnoses and Treatments 
+ Patients can update non-medical personal information such as email address and 
contact number. 
+ Patients are not allowed to modify the past diagnoses, prescribed medications, 
treatments or blood type.. 
+ Appointment Management: 
+ Patients can: 
+ View available appointment slots with doctors. 
+ Schedule Appointments: Choose a doctor, date, and available time slot 
to schedule an appointment. 
+ Reschedule Appointments: Change an existing appointment to a new 
slot, ensuring no conflicts. Upon rescheduling, slot availability will be 
updated automatically. 
+ Cancel Appointments: Cancel an existing appointment. Upon successful 
cancellation, slot availability will be updated automatically. 
+ Patients can view the status of their scheduled appointments. 
+ The status of the appointment will be updated according based on whether the 
doctor accepts of decline the appointment requests (eg: confirmed, canceled, 
completed) 
+ Patients can also view their Appointment Outcome Records of past 
appointments.
 
## 3. Doctor: 
+ Medical Record Management: 
+ Doctors can view the medical records of patients under their care. 
+ Doctors can update the medical records of patients by adding new diagnoses, 
prescriptions, and treatment plans. 
+ Appointment Management: 
+ Doctors can view their personal schedule and set their availability for 
appointments. 
+ Doctors can accept or decline appointment requests. 
+ Doctors can view the list of their upcoming appointments. 
+ Appointment Outcome Record: After each completed appointment, the doctor 
will record the: 
+ Date of Appointment 
+ Type of service provided (e.g., consultation, X-ray, blood test etc). 
+ Any prescribed medications:
+ medication name 
+ status (default is pending) 
+ Consultation notes

## 4. Pharmacist: 
+ Prescription Management: 
+ Pharmacists can view the Appointment Outcome Record to fulfill medication 
prescriptions orders from doctors. 
+ Pharmacists can update the status of prescription in the Appointment Outcome 
Record (e.g., pending to dispensed). 
+ Pharmacists can monitor the inventory of medications, including tracking stock 
levels. 
+ Pharmacists can submit replenishment requests to administrators when stock 
levels are low.

## 5. Administrator: 
+ Staff Management: 
+ Manage hospital staff (doctors and pharmacists) by adding, updating, or 
removing staff members. 
+ Display a list of staff filtered by role, gender, age, etc. 
Appointment Management: 
+ Administrators can access real-time updates of scheduled appointments. 
+ Appointment details should include: 
+ Patient ID 
+ Doctor ID 
+ Appointment status (e.g., confirmed, canceled, completed). 
+ Date and time of appointment 
+ Appointment Outcome Record (for completed appointments) 
+ Inventory Management: 
+ Administrators can view and manage the inventory of medication including, 
adding, removing or updating stock levels. 
+ Administrators can update the low stock level alert line of each medicine. 
+ Administrators can approve replenishment requests from pharmacists. Once the 
request is approved, the stock level will be updated automatically.
 
## System initialization: 
○ The initial staff list can be imported from a file. 
○ The initial patient list can be imported from a file. 
○ The initial inventory, including medicine name, initial stock, low stock level alert 
line can be imported from a file.

## New features implemented
+ Login System
+ new password length of at least 8 characters (lowercase, uppercase, number and special character)
+ Limited Login Attempts are allowed, with a maximum of  3 before the system automatically logs out to mitigate brute force attacks
+ Hashing is used in our login system as an encryption measure for security of User 
+ Passwords will be hidden when the datasheet is viewed externally 


+ Patient Feedback System
+ Patients are able to give Feedback on top of the basic features.
+ Feedback System can be accessed by the staff based on patient feedback.

+ File Path Management 
+ File Path class is added as a centralized source for the file paths of the data files to be used in.
+ Reduces errors within individual classes in ensuring issues like missing or inaccessible files can be detected easily.

+ Symptom Checker Chatbot
+ This feature is used to split minor queries to the chatbot and reduce the patient dependency on the hospital staff.
+ A symptom checker chatbot is implemented by Hugging face API call which is used in our system in SymptomChecker class

+ Real Time Telegram Emergency Notifications
+ This feature is used for patients to send real time Emergency alerts to allow healthcare staff are instantly informed
+ The use of Telegram ensures that alerts are delivered instantly to relevant stakeholders, making the system efficient and responsive


## User Menus 
Each user in the HMS will have a specific menu with options relevant to their role 
upon log in. Below are the menu options for each user role in the system:

## Patient Menu: 
● View Medical Record 
● Update Personal Information
● View Available Appointment Slots 
● Schedule an Appointment 
● Reschedule an Appointment 
● Cancel an Appointment 
● View Scheduled Appointments 
● View Past Appointment Outcome Records 
● Logout 

## Doctor Menu: 
● View Patient Medical Records
● Update Patient Medical Records 
● View Personal Schedule 
● Set Availability for Appointments 
● Accept or Decline Appointment Requests 
● View Upcoming Appointments 
● Record Appointment Outcome 
● Logout 

## Pharmacist Menu: 
● View Appointment Outcome Record 
● Update Prescription Status 
● View Medication Inventory 
● Submit Replenishment Request 
● Logout 

## Administrator Menu: 
● View and Manage Hospital Staff 
● View Appointments details 
● View and Manage Medication Inventory 
● Approve Replenishment Requests 
● Logout 

**[Additional Instructions]**
● Tip: You are encouraged to add more features and functionalities to your 
Hospital Management System. Use your creativity to enhance the user 
experience, improve system performance, or introduce innovative solutions to 
common healthcare challenges. Additional features will be considered during 
evaluation and can help demonstrate your understanding and application of 
Object-Oriented concepts. 
● Assumptions:
○ Document any assumptions made during the development process. 
Discuss how these assumptions influenced your design and what potential 
risks or challenges they may introduce. 
● Development Constraints:
○ The application is to be developed as a Command Line Interface (CLI)
application (non-Graphical User Interface). 
○ Students are required to create their own data files. 
○ No database application (e.g., MySQL, MS Access, etc.) is to be used. 
○ No JSON or XML formats are to be used.

## Contributors
1. @zhihaolaw
2. @UsamaBafana
3. name ()
4. name ()
5. name ()


