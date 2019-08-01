package com.br.catia.desafio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;



public class LerArquivos {
	
	String dadosMercado = "C:\\Users\\Resource\\eclipse-workspace\\testecsv\\dadosmercados.csv";
	String operacoes = "C:\\Users\\Resource\\eclipse-workspace\\testecsv\\operacoes.csv";
	BufferedReader ler = null;
	String linha = "";
	ArrayList<DadosMercado> dados = new ArrayList<>();
	ArrayList<Operacoes> dados2 = new ArrayList<>();
	ArrayList<Resultado> fim = new ArrayList<>();{
		fim.add(null);
		
	}
	
	Resultado fl;
	DadosMercado arc;
	Operacoes op;
	java.util.Date dt1;
	java.util.Date dt2;
	
	String separadorCSV = ";";
	int i =0;
	
	public void leia() throws IOException, NumberFormatException, ParseException{
			leiadadosMercado();
			leiaoperacoes();
			ajuste();
		}
	
	public void leiadadosMercado() throws IOException, NumberFormatException, ParseException{
		ler = new BufferedReader(new FileReader(dadosMercado));
		
		ler.readLine();
		while ((linha = ler.readLine()) != null) {
			
			String[] d = linha.split(separadorCSV);{
				arc = new DadosMercado();
				arc.setId_preco(Integer.parseInt(d[0]));
				arc.setNu_prazo(Integer.parseInt(d[1]));
				arc.setVl_preco(Double.parseDouble(d[2].replace(",", ".")));
			};
			dados.add(arc);

		}
		Collections.sort(dados);

		
		
	}
	
	public void leiaoperacoes() throws IOException, ParseException{
		ler = new BufferedReader(new FileReader(operacoes));
		
		ler.readLine();
		while ((linha = ler.readLine()) != null) {
			
			String[] d = linha.split(separadorCSV);{
				op = new Operacoes();
				op.setCod_op(Integer.parseInt(d[0]));
				op.setDt_inicio(d[1]);
				op.setDt_fim(d[2]);
				op.setDf_Hr(calculohrs(op));
				op.setNm_subg(d[9]);
				op.setQtd(Double.parseDouble(d[12].replace(",", ".")));
				op.setId_Preco(Integer.parseInt(d[13]));			
			};
			dados2.add(op);
		
		}
		Collections.sort(dados2);


	}

	public int calculohrs(Operacoes aop) throws ParseException{
		Integer dt = null;	
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
		sdf.setLenient(false);
		dt1 = sdf.parse (aop.getDt_inicio());
		dt2 = sdf.parse (aop.getDt_fim());
		return dt = (int) ((dt2.getTime() - dt1.getTime() + 3600000)/ 86400000L);
	}

	public void ajuste() throws ParseException{
		
		ArrayList<String> aux = new ArrayList<>();		{
			String aux2="";
			for(int i =0; i<dados2.size();i++){
				
				if(!aux.contains(dados2.get(i).getNm_subg())){
					aux2 = dados2.get(i).getNm_subg();
					aux.add(aux2);;
				}
			}
		
		}
		int cont = 1;
		fl= new Resultado();
		for(Operacoes b: dados2){
			for(DadosMercado a: dados){
				if(a.getId_preco() == b.getId_Preco() && a.getNu_prazo() == b.getDf_hr()){ 
					fl.setResult(fl.getResult()+ (b.getQtd()*a.getVl_preco()));		
					break;
					}
				}
			if(aux.size() != cont-1){
				if(aux.get(cont-1) != b.getNm_subg()){
					fl.setSub(aux.get(cont-1));
					fim.add(fl);
					cont++;
					fl= new Resultado();
				}
			}else{
				fim.remove(0);
			
				break;
			}
		}
		geraCsv();
		
	}
	public void geraCsv(){
		try{
			BufferedWriter writer = new BufferedWriter (new FileWriter ( "C:\\Resultado.csv")); 
			writer.append("Subgrupo");
	        writer.append(',');
	        writer.append("Valor");
	        writer.append('\n');
			for(Resultado f: fim){
				writer.append(f.getSub());
		        writer.append(',');
		        writer.append(String.valueOf(f.getResult()));
		        writer.append('\n');
			}
			writer.flush();
	        writer.close();
			System.out.println("Arquivo finalizado!");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}



