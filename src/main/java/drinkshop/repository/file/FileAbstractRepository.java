package drinkshop.repository.file;

import drinkshop.repository.AbstractRepository;

import java.io.*;

/**
 * File-backed abstract repository.
 * On load: if the file is missing it is created empty; if a line cannot be
 * parsed the entire file is overwritten so the application always starts
 * with consistent data (defect A03 – startup file validation).
 * IOException is no longer silently swallowed (defect A05).
 */
public abstract class FileAbstractRepository<ID, E>
        extends AbstractRepository<ID, E> {

    protected String fileName;

    public FileAbstractRepository(String fileName) {
        this.fileName = fileName;
    }

    protected void loadFromFile() {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                File parent = file.getParentFile();
                if (parent != null) parent.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Nu s-a putut crea fisierul: " + fileName, e);
            }
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                try {
                    E entity = extractEntity(line);
                    super.save(entity);
                } catch (Exception e) {
                    // File has an invalid format – clear state and recreate empty.
                    super.empty();
                    overwriteWithEmpty();
                    return;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Eroare la citirea fisierului: " + fileName, e);
        }
    }

    private void overwriteWithEmpty() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            // intentionally empty – creates a valid, empty file
        } catch (IOException e) {
            throw new RuntimeException("Nu s-a putut suprascrie fisierul: " + fileName, e);
        }
    }

    private void writeToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (E entity : entities.values()) {
                bw.write(createEntityAsString(entity));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Eroare la scrierea fisierului: " + fileName, e);
        }
    }

    @Override
    public E save(E entity) {
        E e = super.save(entity);
        writeToFile();
        return e;
    }

    @Override
    public E delete(ID id) {
        E e = super.delete(id);
        writeToFile();
        return e;
    }

    @Override
    public E update(E entity) {
        E e = super.update(entity);
        writeToFile();
        return e;
    }

    protected abstract E extractEntity(String line);

    protected abstract String createEntityAsString(E entity);
}
