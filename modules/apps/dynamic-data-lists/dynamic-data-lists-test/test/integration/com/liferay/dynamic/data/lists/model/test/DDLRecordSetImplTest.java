/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.dynamic.data.lists.model.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.dynamic.data.lists.helper.DDLRecordSetTestHelper;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.mapping.io.DDMFormJSONSerializerUtil;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.test.util.DDMFormTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMStructureTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMTemplateTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.PortalUtil;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Marcellus Tavares
 */
@RunWith(Arquillian.class)
public class DDLRecordSetImplTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		_ddlRecordSetTestHelper = new DDLRecordSetTestHelper(_group);
	}

	@Test
	public void testGetDDMStructure() throws Exception {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm(
			"Text1", "Text2", "Text3");

		DDMStructure ddmStructure = DDMStructureTestUtil.addStructure(
			_group.getGroupId(), DDLRecordSet.class.getName(), ddmForm);

		DDLRecordSet recordSet = _ddlRecordSetTestHelper.addRecordSet(
			ddmStructure);

		ddmForm = DDMFormTestUtil.createDDMForm("Text2", "Text3");

		DDMTemplate template = DDMTemplateTestUtil.addTemplate(
			_group.getGroupId(), ddmStructure.getStructureId(),
			PortalUtil.getClassNameId(DDLRecordSet.class), "json",
			DDMFormJSONSerializerUtil.serialize(ddmForm), LocaleUtil.US);

		Set<String> fieldNames = ddmStructure.getFieldNames();

		DDMStructure recordSetDDMStructure = recordSet.getDDMStructure(
			template.getTemplateId());

		if (fieldNames.equals(recordSetDDMStructure.getFieldNames())) {
			Assert.fail();
		}

		recordSetDDMStructure = recordSet.getDDMStructure();

		if (!fieldNames.equals(recordSetDDMStructure.getFieldNames())) {
			Assert.fail();
		}
	}

	private DDLRecordSetTestHelper _ddlRecordSetTestHelper;

	@DeleteAfterTestRun
	private Group _group;

}