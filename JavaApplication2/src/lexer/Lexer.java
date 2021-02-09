package lexer;

import java.io.*;
import java.util.*;

import symbols.*;

public class Lexer {
	public static int line = 1;		
	char peek = ' ';		
	Hashtable<String, Word> words = 
		new Hashtable<String, Word>();
	
	private Hashtable<Token, String> table = 
		new Hashtable<Token, String>();
	
	private List<String> tokens = 
		new LinkedList<String> ();
	
	BufferedReader reader = null; 
	
	private Boolean isEnd = false;
	
	
	public Boolean getReaderState() {
		return this.isEnd;
	}
	

	public void saveSymbolsTable() throws IOException {
		FileWriter writer = new FileWriter("t.txt");
		writer.write("[=]\n");
		writer.write("\r\n");
		
		Enumeration<Token> e = table.keys();
		while( e.hasMoreElements() ){
			Token token = (Token)e.nextElement();
			String desc = table.get(token);
			
			=
			writer.write(token + "\t\t\t" + desc + "\r\n");
		}
			
		writer.flush();
	}
	
	=
	public void saveTokens() throws IOException {
		FileWriter writer = new FileWriter("Tokens.txt");
		writer.write("[=]	\n");
		writer.write("\r\n");
		
		for(int i = 0; i < tokens.size(); ++i) {
			String tok = (String)tokens.get(i);
			
			
			writer.write(tok + "\r\n");
		}	
			
		writer.flush();
	}
	
	void reserve(Word w) {
		words.put(w.lexme, w);
	}
	
	
	public Lexer() {
		
		try {
			reader = new BufferedReader(new FileReader("t.txt"));
		}
		catch(IOException e) {
			System.out.print(e);
		}
		
	
		this.reserve(new Word("if", Tag.IF));
		this.reserve(new Word("then", Tag.THEN));
		this.reserve(new Word("else", Tag.ELSE));
		this.reserve(new Word("while", Tag.WHILE));
		this.reserve(new Word("do", Tag.DO));
		
		
		this.reserve(Word.True);
		this.reserve(Word.False);
		this.reserve(Type.Int);
		this.reserve(Type.Char);
		this.reserve(Type.Bool);
		this.reserve(Type.Float);
	}
	
	public void readch() throws IOException {
		
		peek = (char)reader.read();
		if((int)peek == 0xffff){
			this.isEnd = true;
		}
		// peek = (char)System.in.read();
	}
	
	public Boolean readch(char ch) throws IOException {
		readch();
		if (this.peek != ch) {
			return false;
		}
		
		this.peek = ' ';
		return true;
	}
	
	public Token scan() throws IOException {
		
		for( ; ; readch() ) {
			if(peek == ' ' || peek == '\t')
				continue;
			else if (peek == '\n') 
				line = line + 1;
			else
				break;
		}
		
		
		switch (peek) {
		
		case '=' :
			if (readch('=')) {
				tokens.add("==");
				return Word.eq;	
			}
			else {
				tokens.add("=");
				return new Token('=');
			}
		case '>' :
			if (readch('=')) {
				tokens.add(">=");
				return Word.ge;
			}
			else {
				tokens.add(">");
				return new Token('>');
			}
		case '<' :
			if (readch('=')) {
				tokens.add("<=");
				return Word.le;
			}
			else {
				tokens.add("<");
				return new Token('<');
			}
		case '!' :
			if (readch('=')) {
				tokens.add("!=");
				return Word.ne;
			}
			else {
				tokens.add("!");
				return new Token('!');
			}	
		}
		
		
		if(Character.isDigit(peek)) {
			int value = 0;
			do {
				value = 10 * value + Character.digit(peek, 10);
				readch();
			} while (Character.isDigit(peek));
			
			Num n = new Num(value);
			tokens.add(n.toString());
			//table.put(n, "Num");
			return n;
		}
		
		
		if(Character.isLetter(peek)) {
			StringBuffer sb = new StringBuffer();
			
			
			do {
				sb.append(peek);
				readch();
			} while (Character.isLetterOrDigit(peek));
			
			
			String s = sb.toString();
			Word w = (Word)words.get(s);
			
			
			if(w != null) {
				// table.put(w, "KeyWord or Type");
				tokens.add(w.toString());
				return w; 
			}
			
			
			w = new Word(s, Tag.ID);
			tokens.add(w.toString());
			table.put(w, "id");
			words.put(s,  w);
			
			return w;
		}
		
		
		Token tok  = new Token(peek);
		// table.put(tok, "Token or Seprator");
		if ((int)peek != 0xffff ) 
			tokens.add(tok.toString());
		peek = ' ';
		
		return tok;
	}

    private void reserve(Type Int) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}