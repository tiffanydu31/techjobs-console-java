package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAllField(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        sortData(values);

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll(String sortChoice) {

        // load data, if not already loaded
        loadData();
        sortData(allJobs, sortChoice);

        return copyData(allJobs);


    }

    public static ArrayList<HashMap<String, String>> findByValue(String value, String sortChoice) {
        loadData();
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
        for (HashMap<String, String> row : allJobs) {
            Boolean add = false;
            for (Map.Entry<String, String> column : row.entrySet()) {
                if (column.getValue().toLowerCase().contains(value.toLowerCase())) {
                    add = true;
                }
            }
            if (add) {
                jobs.add(row);
            }
        }

        sortData(jobs, sortChoice);
        return copyData(jobs);
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value, String sortChoice) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column);

            if (aValue.toLowerCase().contains(value.toLowerCase())) {
                jobs.add(row);
            }
        }

         sortData(jobs, sortChoice);

        return copyData(jobs);
    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

    public static void sortData(ArrayList<HashMap<String, String>> jobs, String sortByKey) {
        Collections.sort(jobs, new Comparator<Map<String, String>>() {
            @Override
            public int compare(final Map<String, String> map1, final Map<String, String> map2) {
                // Do your sorting...
                return (map1.get(sortByKey).compareTo(map2.get(sortByKey)));
            }
        });
    }

    public static void sortData(ArrayList<String> values) {
        Collections.sort(values);
    }

    public static ArrayList<HashMap<String, String>> copyData(ArrayList<HashMap<String, String>> originalJobs) {
        ArrayList<HashMap<String, String>> copiedJobs = new ArrayList<>(originalJobs);

        return copiedJobs;
    }
}