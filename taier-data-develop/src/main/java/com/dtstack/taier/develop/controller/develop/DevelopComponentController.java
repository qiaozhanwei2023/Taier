/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dtstack.taier.develop.controller.develop;

import com.dtstack.taier.common.lang.coc.APITemplate;
import com.dtstack.taier.common.lang.web.R;
import com.dtstack.taier.develop.service.develop.impl.BatchComponentService;
import com.dtstack.taier.develop.vo.develop.query.BatchComponentGetDatabasesVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "集群组件信息管理", tags = {"集群组件信息管理"})
@RestController
@RequestMapping(value = "/batchComponent")
public class DevelopComponentController {

    @Autowired
    private BatchComponentService batchComponentService;

    @PostMapping(value = "getAllDatabases")
    @ApiOperation(value = "获取所有的databases")
    public R<List<String>> getFunction(@RequestBody BatchComponentGetDatabasesVO vo) {
        return new APITemplate<List<String>>() {
            @Override
            protected List<String> process() {
                return batchComponentService.getAllDataBases(vo.getClusterId(), vo.getComponentTypeCode());
            }
        }.execute();
    }

}
