import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

class FitnessTracker {
    private static final String FILE_NAME = "fitness_data.txt";
    private static Map<String, Integer> workouts = new HashMap<>(); // Stores workout and duration (mins)
    private static Map<String, Integer> exercises = new HashMap<>(); // Stores exercise and reps
    private static Set<String> favoriteWorkouts = new HashSet<>(); // Stores favorite workouts
    private static int totalSteps = 0;
    private static double waterIntake = 0;
    private static double weight = 0;
    private static double height = 0;

    public static void main(String[] args) {
        loadFitnessData();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n1. Log Workout\n2. Log Exercise\n3. Mark Favorite Workout\n4. Log Steps\n5. Log Water Intake\n6. Set Weight & Height\n7. Calculate BMI\n8. View Progress\n9. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    logWorkout(scanner);
                    break;
                case 2:
                    logExercise(scanner);
                    break;
                case 3:
                    markFavoriteWorkout(scanner);
                    break;
                case 4:
                    logSteps(scanner);
                    break;
                case 5:
                    logWaterIntake(scanner);
                    break;
                case 6:
                    setWeightHeight(scanner);
                    break;
                case 7:
                    calculateBMI();
                    break;
                case 8:
                    viewProgress();
                    break;
                case 9:
                    saveFitnessData();
                    System.out.println("Exiting... Stay healthy!");
                    return;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    private static void logWorkout(Scanner scanner) {
        System.out.print("Enter workout name: ");
        String workout = scanner.nextLine();
        System.out.print("Enter duration (minutes): ");
        int duration = scanner.nextInt();
        scanner.nextLine();
        
        workouts.put(workout, workouts.getOrDefault(workout, 0) + duration);
        System.out.println("Workout logged successfully!");
    }

    private static void logExercise(Scanner scanner) {
        System.out.print("Enter exercise name: ");
        String exercise = scanner.nextLine();
        System.out.print("Enter number of reps: ");
        int reps = scanner.nextInt();
        scanner.nextLine();
        
        exercises.put(exercise, exercises.getOrDefault(exercise, 0) + reps);
        System.out.println("Exercise logged successfully!");
    }

    private static void markFavoriteWorkout(Scanner scanner) {
        System.out.print("Enter workout to mark as favorite: ");
        String workout = scanner.nextLine();
        
        if (workouts.containsKey(workout)) {
            favoriteWorkouts.add(workout);
            System.out.println(workout + " added to favorites!");
        } else {
            System.out.println("Workout not found! Log it first.");
        }
    }

    private static void logSteps(Scanner scanner) {
        System.out.print("Enter step count for today: ");
        int steps = scanner.nextInt();
        scanner.nextLine();
        
        totalSteps += steps;
        System.out.println("Steps logged successfully!");
    }

    private static void logWaterIntake(Scanner scanner) {
        System.out.print("Enter water intake in liters: ");
        double water = scanner.nextDouble();
        scanner.nextLine();
        
        waterIntake += water;
        System.out.println("Water intake logged successfully!");
    }

    private static void setWeightHeight(Scanner scanner) {
        System.out.print("Enter weight (kg): ");
        weight = scanner.nextDouble();
        System.out.print("Enter height (m): ");
        height = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.println("Weight and height updated!");
    }

    private static void calculateBMI() {
        if (weight > 0 && height > 0) {
            double bmi = weight / (height * height);
            System.out.println("Your BMI is: " + String.format("%.2f", bmi));
        } else {
            System.out.println("Set weight and height first!");
        }
    }

    private static void viewProgress() {
        System.out.println("\nFitness Progress Summary:");
        System.out.println("Total Workouts Logged: " + workouts.size());
        System.out.println("Total Exercises Logged: " + exercises.size());
        System.out.println("Total Steps: " + totalSteps);
        System.out.println("Total Water Intake: " + waterIntake + " liters");
        if (!favoriteWorkouts.isEmpty()) {
            System.out.println("Favorite Workouts: " + favoriteWorkouts);
        }
    }

    private static void saveFitnessData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write("Weight," + weight + "\n");
            writer.write("Height," + height + "\n");
            writer.write("Steps," + totalSteps + "\n");
            writer.write("Water," + waterIntake + "\n");
            for (String workout : workouts.keySet()) {
                writer.write("Workout," + workout + "," + workouts.get(workout) + "\n");
            }
            for (String exercise : exercises.keySet()) {
                writer.write("Exercise," + exercise + "," + exercises.get(exercise) + "\n");
            }
            for (String favorite : favoriteWorkouts) {
                writer.write("Favorite," + favorite + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving fitness data: " + e.getMessage());
        }
    }

    private static void loadFitnessData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    switch (parts[0]) {
                        case "Weight": weight = Double.parseDouble(parts[1]); break;
                        case "Height": height = Double.parseDouble(parts[1]); break;
                        case "Steps": totalSteps = Integer.parseInt(parts[1]); break;
                        case "Water": waterIntake = Double.parseDouble(parts[1]); break;
                    }
                } else if (parts.length == 3) {
                    if (parts[0].equals("Workout")) {
                        workouts.put(parts[1], Integer.parseInt(parts[2]));
                    } else if (parts[0].equals("Exercise")) {
                        exercises.put(parts[1], Integer.parseInt(parts[2]));
                    }
                } else if (parts[0].equals("Favorite")) {
                    favoriteWorkouts.add(parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("No previous data found. Starting fresh.");
        }
    }
}
