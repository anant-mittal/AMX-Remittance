package com.bootloaderjs;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.amx.jax.dict.Language;
import com.amx.utils.ArgUtil;

//@RunWith(SpringRunner.class)
@SpringBootTest
public class LanguageTest {

	@Test
	public void testLanguageEnumFromNumber() {
		Language lang = Language.fromId(Language.PH.getBDCode());
		assertTrue("Lang is not TL", lang == Language.TL);
	}

	@Test
	public void testLanguageFromAlias() {
		Language lang = Language.fromString("ph", Language.EN);
		assertTrue("Lang is not TL", lang == Language.TL);
	}

	@Test
	public void testLanguageFromStringWithDefault() {
		Language lang = (Language) ArgUtil.parseAsEnum("ph", Language.EN, Language.class);
		assertTrue("Lang is not PH", lang == Language.PH);
	}

	@Test
	public void testLanguageFromString() {
		Language lang = (Language) ArgUtil.parseAsEnum("ph", Language.class);
		assertTrue("Lang is not PH", lang == Language.PH);
	}
}
