package com.amx.jax.postman.custom;

//public class PanelTagProcessor extends AbstractProcessor {
//
//	public PanelTagProcessor(TemplateMode templateMode, int precedence) {
//		super(templateMode, precedence);
//	}
//
//	private static final String TAG_NAME = "panel";
//	private static final int PRECEDENCE = 10000;
//
//	public static Element form = new Element("form");
//
//	@Override
//	public ProcessorResult doProcess(Arguments arguments, ProcessorMatchingContext context, Node node) {
//		form.setProcessable(true);
//		form.setAttribute("role", "form");
//		form.setAttribute("class", "form");
//		form.setAttribute("action", "");
//		form.setAttribute("method", "post");
//		node.getParent().insertBefore(node, form);
//
//		List<Element> lista = node.getParent().getElementChildren();
//		for (Element child : lista) {
//			if (!child.getOriginalName().equals("form"))
//				child.moveAllChildren(form);
//		}
//
//		List<Element> lista2 = form.getElementChildren();
//		for (Element child : lista2) {
//			child.setProcessable(true);
//		}
//
//		node.getParent().removeChild(node);
//		return ProcessorResult.OK;
//	}
//
//	@Override
//	public int getPrecedence() {
//		return 0;
//	}
//
//	@Override
//	public IProcessorMatcher<? extends Node> getMatcher() {
//		return new ElementNameProcessorMatcher("form");
//	}
//}