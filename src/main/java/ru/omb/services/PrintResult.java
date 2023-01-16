package ru.omb.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.omb.Output;
import ru.omb.Statistics;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class PrintResult implements PrintSolveTask{
    @Override
    public void printResult(Output output, HashMap<String, Statistics> statistic) throws JAXBException, IOException {
        switch (output.ordinal()) {
            case 0:
                printPlain(statistic);
                break;
            case 1:
                printXml(statistic);
                break;
            case 2:
                printJson(statistic);
                break;
            default:
        }
    }

    private void printPlain(HashMap<String, Statistics> statistics) {
        for (Map.Entry<String, Statistics> pair : statistics.entrySet())  {
            String key = pair.getKey();
            Statistics value = pair.getValue();
            System.out.println("Расширение файла...........: " + key);
            System.out.println("Количество файлов..........= " + value.getCount());
            System.out.println("Размер файлов..............= " + value.getSize());
            System.out.println("Вcего строк................= " + value.getCountAllLine());
            System.out.println("Количество непустых строк..= " + value.getCountNonEmptyLine());
            System.out.println("Количество комментарий.....= " + value.getCountCommentLine());
            System.out.println();
        }
    }

    private void printXml(HashMap<String, Statistics> statistics) throws JAXBException {
        for (Map.Entry<String, Statistics> pair : statistics.entrySet())  {
            String key = pair.getKey();
            Statistics value = pair.getValue();
            System.out.println("Расширение файла...........: " + key);
            StringWriter writer = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(Statistics.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(value, writer);
            String result = writer.toString();
            System.out.println(result);
            System.out.println();
        }
    }

    private void printJson(HashMap<String, Statistics> statistics) throws IOException {
        for (Map.Entry<String, Statistics> pair : statistics.entrySet())  {
            String key = pair.getKey();
            Statistics value = pair.getValue();
            System.out.println("Расширение файла...........: " + key);
            StringWriter writer = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(writer, value);
            String result = writer.toString();
            System.out.println(result);
            System.out.println();
        }
    }
}
