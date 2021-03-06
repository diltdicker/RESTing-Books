package edu.asupoly.ser422.restexample.services.impl;

import java.util.List;
import java.util.ArrayList;

import edu.asupoly.ser422.restexample.model.Author;
import edu.asupoly.ser422.restexample.model.Book;
import edu.asupoly.ser422.restexample.model.Subject;
import edu.asupoly.ser422.restexample.services.BooktownService;

//A simple impl of interface BooktownService
public class SimpleBooktownServiceImpl implements BooktownService {
	
	// Author section
	private final static String[] fnames = {"Laura", "Hillary", "Jackie",};
	private final static String[] lnames = {"Bush", "Clinton", "Kennedy"};
	private ArrayList<Author> __authors = null;

	public List<Author> getAuthors() {
		List<Author> deepClone = new ArrayList<Author>();
		for (Author a : __authors) {
			deepClone.add(new Author(a.getAuthorId(), a.getLastName(), a.getFirstName()));
		}
		return deepClone;
	}

	public int createAuthor(String lname, String fname) {
		int max = -1;
		for (Author a : __authors) {
			if (a.getAuthorId() > max) {
				max = a.getAuthorId();
			}
		}
		__authors.add(new Author(max+1, lname, fname));
		return max+1;
	}

	public boolean deleteAuthor(int authorId) {
		boolean rval = false;
		try {
			// Find any Books pointing at this author
			List<Book> books = new ArrayList<Book>();
			for (Book b : __books) {
				if (b.getAuthorId() == authorId) {
					b.setAuthorId(-1);  // I guess -1 will mean marked for deletion
					books.add(b);
				}
			}
			Author a = __authors.remove(authorId);
			if (!(rval = (a != null))) {
				// if we couldn't remove that book we have to undo the books above,
				// which is why we hung onto them!
				for (Book b : books) {
					b.setAuthorId(authorId);
				}
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			rval = false;
		}
		return rval;
	}
	
	@Override
	public Author getAuthor(int id) {
		for (Author a : __authors) {
			if (a.getAuthorId() == id) {
				return a;
			}
		}
		return null;
	}

	@Override
	public boolean updateAuthor(Author author) {
		boolean rval = false;
		for (Author a : __authors) {
			if (a.getAuthorId() == author.getAuthorId()) {
				rval = true;
				a.setFirstName(author.getFirstName());
				a.setLastName(author.getLastName());
			}
		}
		return rval;
	}
	
    // Book section
	private final static String[] titles = {"Sisters First", "My Turn", "Four Days"};
	private ArrayList<Book> __books = null;

    public List<Book> getBooks() {
		List<Book> deepClone = new ArrayList<Book>();
		for (Book b : __books) {
			deepClone.add(new Book(b.getBookId(), b.getTitle(), b.getAuthorId(), b.getSubjectId()));
		}
		return deepClone;
    }
    public Book getBook(int id) {
		for (Book b : __books) {
			if (b.getBookId() == id) {
				return b;
			}
		}
    		return null;	
    }
    public int createBook(String title, int aid, int sid) {
		int max = -1;
		for (Book b : __books) {
			if (b.getBookId() > max) {
				max = b.getBookId();
			}
		}
		__books.add(new Book(max+1, title, aid, sid));
		return max+1;
    }
    public Author findAuthorOfBook(int bookId) {
    		Author a = null;
    		Book b = getBook(bookId);
    		if (b != null) {
    			a = getAuthor(b.getAuthorId());
    		}
    		return a;
    }
    
    // Subject section
	private final static String[] subjects = {"Humor", "Politics", "Drama"};
	private final static String[] locations = {"Midland, TX", "Little Rock, AR", "Dallas, TX"};
	private ArrayList<Subject> __subjects = null;
	
    public List<Subject> getSubjects() {
		List<Subject> deepClone = new ArrayList<Subject>();
		for (Subject s : __subjects) {
			deepClone.add(new Subject(s.getSubjectId(), s.getSubject(), s.getLocation()));
		}
		return deepClone;
    }
    public Subject getSubject(int id) {
    		for (Subject s : __subjects) {
    			if (s.getSubjectId() == id) {
    				return s;
    			}
    		}
    		return null;
    }
    public List<Book> findBooksBySubject(int subjectId) {
    	List<Book> deepClone = new ArrayList<Book>();
    	for (Book b: __books) {
    		if (b.getSubjectId() == subjectId) {
    			deepClone.add(new Book(b.getBookId(), b.getTitle(), b.getAuthorId(), b.getSubjectId()));
    		}
    	}
    	return deepClone;
    }
    
	// Only instantiated by factory?
	public SimpleBooktownServiceImpl() {
		__authors = new ArrayList<Author>();
		__books = new ArrayList<Book>();
		__subjects = new ArrayList<Subject>();
		for (int i = 0; i < fnames.length; i++) {
			Author a = new Author(i, lnames[i], fnames[i]);
			__authors.add(a);
			Subject s = new Subject(i, subjects[i], locations[i]);
			__subjects.add(s);
			__books.add(new Book(i, titles[i], a.getAuthorId(), s.getSubjectId()));
		}
	}

	@Override
	public boolean deleteBook(int bookID) {
		boolean rval = false;
		try {
			Book b = __books.remove(bookID);
			if (!(rval = (b != null))) {
				//do nothing
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			rval = false;
		}
		return rval;
	}

	@Override
	public List<Author> getAuthorsBySubject(String location) {
		List<Author> authors = new ArrayList<Author>();
		// Determine the subject(s)
		if (location.length() > 0) {
			ArrayList<Integer> subjecNums = new ArrayList<Integer>();
			ArrayList<Integer> authorIds = new ArrayList<Integer>();
			for (Subject s: __subjects) {
				if (s.getLocation().contains(location)) {
					subjecNums.add(s.getSubjectId());
					System.out.println(s);
				}
			}
			for (int i = 0; i < subjecNums.size(); i++) {
				for (Book b: __books) {
					if (b.getSubjectId() == subjecNums.get(i)) {
						if (authorIds.indexOf(b.getAuthorId()) == -1) {
							authorIds.add(b.getAuthorId());
						}
					}
				}
			}
			for (int i = 0; i < authorIds.size(); i++) {
				for (Author a: __authors) {
					if (a.getAuthorId() == authorIds.get(i)) {
						authors.add(a);
					}
				}
			}
		}
		//List<Author> authors = getAuthors();
		return authors;
	}

	@Override
	public boolean updateSubject(Subject s) {
		boolean rval = false;
		for (Subject old : __subjects) {
			if (s.getSubjectId() == old.getSubjectId()) {
				rval = true;
				old.setLocation(s.getLocation());
				old.setSubject(s.getSubject());
			}
		}
		return rval;
	}

	@Override
	public boolean updateSubjectLocation(Subject s) {
		boolean rval = false;
		for (Subject old : __subjects) {
			if (s.getSubjectId() == old.getSubjectId()) {
				rval = true;
				old.setLocation(s.getLocation());
				old.setSubject(old.getSubject());
			}
		}
		return rval;
	}

}
