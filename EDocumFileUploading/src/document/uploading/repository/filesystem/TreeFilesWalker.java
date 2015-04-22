/**
 * 
 */
package document.uploading.repository.filesystem;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.FileVisitOption;
import java.util.EnumSet;
import java.util.logging.Logger;
import document.uploading.enums.EDevisions;
import document.uploading.enums.EDocumentTypes;

/**
 * @author azhuk
 * Creation date: Aug 4, 2014
 *
 */
public class TreeFilesWalker extends SimpleFileVisitor<Path> {
	
	private static final Logger _logger = Logger.getLogger(TreeFilesWalker.class
			.getCanonicalName());
	
	int _repositoryNameStartIndex;
	int _yearIndex;
	int _docType; 
	int _devision; 
	
	FileProcessor _fprocessor;
	//final int _metaNameCoubt =  
			
	public TreeFilesWalker (int repositoryNameStartIndex, FileProcessor reader) {
		
		_fprocessor = reader;
		_repositoryNameStartIndex=repositoryNameStartIndex;
		
		_yearIndex = _repositoryNameStartIndex+1;
		_docType = _repositoryNameStartIndex+2;
		_devision =_repositoryNameStartIndex +3;		
		
	}
	

    //Выводим информацию о обрабатываемом в данный момент файле.
// метод Files.probeContentType выводит информацию о типе контента
	
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
      	
    	
        if (attr.isSymbolicLink()) {        	
        	//_logger.info (String.format("Symbolic link: %s ", file));            
            
        } else if (attr.isRegularFile()) {
        	_logger.info (String.format("Regular file: %s Content is %s%n ", file,Files.probeContentType(file)));
            	_fprocessor.process(file);
        } else {
        	_logger.info (String.format("Other: %s ", file));
        }
        //_logger.info("(" + attr.size() + " bytes)");
        
        return FileVisitResult.CONTINUE;
    }
 
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
        throws IOException    {
    	FileVisitResult toReturn = super.preVisitDirectory(dir, attrs);
    	_logger.info("Visit to Folder:"+dir);
    	//_logger.info("Directory is: "+ dir.getFileName() );
    	if (dir.getNameCount() ==_devision+1) {    	
    	
    		_fprocessor.setProcessedAdditions(false);
    		_fprocessor.setYear(dir.getName(_yearIndex).toString());
    		_fprocessor.setDocumentTypes(EDocumentTypes.fromString(dir.getName(_docType).toString()));
    		_fprocessor.setDevisions( EDevisions.parseString(dir.getName(_devision).toString()) );
    		
      /*
		_logger.info("Year: "+  _fprocessor.getYear() );
		_logger.info("Doc type: "+ _fprocessor.getDocumentTypes());
		_logger.info("Devision: "+ _fprocessor.getDevisions());
		*/
    	} else if (dir.getNameCount() == _devision+2) {   
    		_fprocessor.setProcessedAdditions(true);	
    	}
    	return toReturn;
    }
    
    //Выводим информацию о посещенном каталоге
    @Override
/* Перечисление FileVisitResult имеет следующие варианты
CONTINUE продолжить проход.
SKIP_SIBLINGS продолжить проход без осмотра дочерних папок.
SKIP_SUBTREE продолжить без просмотра объектов данной папки.
TERMINATE заверщить.
*/
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        //System.out.format("Directory: %s%n", dir);
    	_fprocessor.setProcessedAdditions(false);	
        _logger.info( String.format("Directory: %s%n", dir));
        return FileVisitResult.CONTINUE;
    }
    
    
    
    //в случае ошибки доступа к файлу выбрасывается исключение IOException
       @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {        
        _logger.severe(exc.getMessage());
        return FileVisitResult.CONTINUE;
    }
       
       
       
       
    public static void main(String[] args) throws IOException{
        //создаем объект Path
        Path startingDir = Paths.get(args[0]);
        int  startRepositoryDir = Integer.valueOf( args[1]).intValue();
        //создаем экземпляр нашего класса, реализующего FileVisit
        TreeFilesWalker pf = new TreeFilesWalker(startRepositoryDir,
        			new FileProcessor());
 //
        //создаем экземпляр EnumSet, необходимый нам как параметр, и указывающий,
// что программа при  прохождении дерева файлов, следует по ссылкам
        EnumSet<FileVisitOption> options = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
       int maxDepth = 5; //максимальное число уровней каталога для просмотра
       /* Запуск анализа дерева файлов. Используется один из методов класса Files*/
       Files.walkFileTree(startingDir, options, maxDepth, pf);
       //Files.walkFileTree(startingDir,  pf);
 
    }
} 




