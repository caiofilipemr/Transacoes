package com.transacoes;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

@SpringBootApplication
public class TransacoesApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransacoesApplication.class, args);
	}

	@InitBinder
	public void binder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
	    public void setAsText(String value) {
	        try {
	            setValue(new SimpleDateFormat("dd/MM/yyyy").parse(value));
	        } catch(ParseException e) {
	            setValue(null);
	        }
	    }

	    public String getAsText() {
	        return new SimpleDateFormat("dd/MM/yyyy").format((Date) getValue());
	    }
		});
	}
}
