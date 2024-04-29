package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Ravis230987";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection,scanner);
            while(true){
                System.out.println("Hospital Management System");
                System.out.println("1. Add Patient");
                System.out.println("2. Add Doctors");
                System.out.println("3. View Patients");
                System.out.println("4. View Doctors");
                System.out.println("5. Book Appointment");
                System.out.println("6. Exit");
                System.out.println("Enter your choice: ");
                int choice = scanner.nextInt();
                switch(choice){
                    case 1:
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        doctor.addDoctor();
                        break;
                        case 3:
                            patient.viewPatients();
                            System.out.println();
                            break;
                            case 4:
                                doctor.viewDoctors();
                                System.out.println();
                                break;
                                case 5:
                                    bookAppointment(patient,doctor,connection,scanner);
                                    System.out.println();
                                    break;
                    case 6:
                        System.out.println("Thank YOU! FOR USING HOSPITAL MANAGEMENT SYSTEM !!");
                        return;
                    default:
                        System.out.println("Enter valid choice!!");
                        break;
                }
            }



        }catch(SQLException e){
            e.printStackTrace();

        }


    }
    public static void bookAppointment(Patient patient,Doctor doctor,Connection connection,Scanner scanner){
        System.out.print("Enter Patient id: ");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor id: ");
        int doctorId = scanner.nextInt();
        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();
        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if(checkDoctorAvailability(doctorId, appointmentDate, connection)){
                String appointmentmentQuery = "INSERT INTO appointments (patient_id, doctor_id, appointment_date) Values (?,?,?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("Appointment booked!");
                    }else{
                        System.out.println("Failed to Book Appointment!");
                    }

                }catch (SQLException e) {
                    e.printStackTrace();;
                }
            } else{
                System.out.println("Doctor not available on this date!!");
            }
        }else{
            System.out.println("Either doctor or patient doesn't exist!!!");
        }
    }
    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection){
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, doctorId);
            ps.setString(2,appointmentDate);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int count = rs.getInt(1);
                if(count == 0){
                    return true;
                }else{
                    return false;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    }

