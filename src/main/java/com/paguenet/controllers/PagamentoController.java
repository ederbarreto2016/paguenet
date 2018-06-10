package com.paguenet.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paguenet.models.Cliente;
import com.paguenet.models.Pagamento;
import com.paguenet.repository.ClienteRepository;
import com.paguenet.repository.PagamentoRepository;

@Controller
public class PagamentoController {
	
	@Autowired
	private PagamentoRepository pr;
	
	@Autowired
	private ClienteRepository cr;
	
	@RequestMapping(value="/cadastrarPagamento", method=RequestMethod.GET)
	public String form() {
		return "pagamento/formPagamento";
	}
	
	@RequestMapping(value="/cadastrarPagamento", method=RequestMethod.POST)
	public String form(@Valid Pagamento pagamento, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/cadastrarPagamento";
		}
		pr.save(pagamento);
		attributes.addFlashAttribute("mensagem", "Pagamento cadastrado com sucesso!");
		return "redirect:/cadastrarPagamento";
	}
	
	@RequestMapping("/pagamentos")
	public ModelAndView listaPagamentos() {
		ModelAndView mv = new ModelAndView("index");
		Iterable<Pagamento> pagamentos = pr.findAll();
		mv.addObject("pagamentos", pagamentos);
		return mv;
	}
	
	@RequestMapping(value="/{codigo}", method=RequestMethod.GET)
	public ModelAndView detalhesPagamento(@PathVariable("codigo") long codigo) {
		Pagamento pagamento = pr.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("pagamento/detalhesPagamento");
		mv.addObject("pagamento", pagamento);
		
		Iterable<Cliente> clientes = cr.findByPagamento(pagamento);
		mv.addObject("clientes", clientes);
		return mv;
	}
	
	@RequestMapping("/deletarPagamento")
	public String deletarPagamento(long codigo) {
		Pagamento pagamento = pr.findByCodigo(codigo);
		pr.delete(pagamento);
		return "redirect:/pagamentos";
	}
	
	@RequestMapping(value="/{codigo}", method=RequestMethod.POST)
	public String detalhesPagamentoPost(@PathVariable("codigo") long codigo, @Valid Cliente cliente, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/{codigo}";
		}
		Pagamento pagamento = pr.findByCodigo(codigo);
		cliente.setPagamento(pagamento);
		cr.save(cliente);
		attributes.addFlashAttribute("mensagem", "Cliente adicionado com sucesso!");
		return "redirect:/{codigo}";
	}
	
	@RequestMapping("/deletarCliente")
	public String deletarCliente(long idCliente) {
		Cliente cliente = cr.findByIdCliente(idCliente);
		cr.delete(cliente);
		 
		Pagamento pagamento = cliente.getPagamento();
		long codigoLong = pagamento.getCodigo();
		String codigo = "" + codigoLong;
		return "redirect:/" + codigo;
	}
}
