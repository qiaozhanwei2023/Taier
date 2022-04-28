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
import com.dtstack.taier.dao.domain.ScheduleJobHistory;
import com.dtstack.taier.dao.pager.PageResult;
import com.dtstack.taier.develop.dto.devlop.FlinkServerLogVO;
import com.dtstack.taier.develop.dto.devlop.FlinkSqlTaskManagerVO;
import com.dtstack.taier.develop.dto.devlop.RuntimeLogResultVO;
import com.dtstack.taier.develop.dto.devlop.ServerLogsVO;
import com.dtstack.taier.develop.dto.devlop.TaskResourceParam;
import com.dtstack.taier.develop.service.develop.impl.FlinkServerLogService;
import com.dtstack.taier.develop.service.develop.impl.FlinkSqlRuntimeLogService;
import com.dtstack.taier.develop.service.develop.impl.FlinkSqlTaskService;
import com.dtstack.taier.develop.vo.develop.query.CheckResultVO;
import com.dtstack.taier.develop.vo.develop.query.GetFailoverLogsByTaskIdVO;
import com.dtstack.taier.develop.vo.develop.query.RuntimeLogQueryVO;
import com.dtstack.taier.develop.vo.develop.query.StartFlinkSqlVO;
import com.dtstack.taier.develop.vo.develop.query.TaskIdQueryVO;
import com.dtstack.taier.develop.vo.develop.query.TaskJobHistorySearchVO;
import com.dtstack.taier.develop.vo.develop.query.TaskSearchVO;
import com.dtstack.taier.develop.vo.develop.query.TaskSqlFormatVO;
import com.dtstack.taier.develop.vo.develop.query.TaskStatusSearchVO;
import com.dtstack.taier.develop.vo.develop.result.StartFlinkSqlResultVO;
import com.dtstack.taier.develop.vo.develop.result.TaskListResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Api(value = "FlinkSQL任务管理", tags = {"FlinkSQL任务管理"})
@RestController
@RequestMapping(value = "/flinkSql")
public class DevelopFlinkSQLController {

    @Autowired
    private FlinkSqlTaskService flinkSqlTaskService;

    @Autowired
    private FlinkSqlRuntimeLogService flinkSqlRuntimeLogService;

    @Autowired
    private FlinkServerLogService flinkServerLogService;


    @ApiOperation(value = "运行FlinkSQL任务")
    @PostMapping(value = "start")
    public R<StartFlinkSqlResultVO> startFlinkSql(@RequestBody StartFlinkSqlVO vo) {
        return R.ok(flinkSqlTaskService.startFlinkSql(vo.getTaskId(), vo.getExternalPath()));
    }


    @ApiOperation(value = "停止FlinkSQL任务")
    @PostMapping(value = "stop")
    public R<Boolean> stopFlinkSql(@RequestBody StartFlinkSqlVO vo) {
        return R.ok(flinkSqlTaskService.stopStreamTask(vo.getTaskId()));
    }

    @PostMapping(value = "/grammarCheck")
    @ApiOperation(value = "语法检测")
    public R<CheckResultVO> grammarCheck(@RequestBody TaskResourceParam taskResourceParam) {
        return new APITemplate<CheckResultVO>() {
            @Override
            protected CheckResultVO process() {
                return flinkSqlTaskService.grammarCheck(taskResourceParam);
            }
        }.execute();
    }

    @ApiOperation("获取所有时区信息")
    @PostMapping(value = "getAllTimeZone")
    public R<List<String>> getAllTimeZone() {
        return new APITemplate<List<String>>() {
            @Override
            protected List<String> process() {
                return flinkSqlTaskService.getAllTimeZone();
            }
        }.execute();
    }

    @ApiOperation("根据条件查询任务列表")
    @PostMapping(value = "getTaskList")
    public R<PageResult<List<TaskListResultVO>>> getTaskList(@RequestBody TaskSearchVO taskSearchVO) {
        return new APITemplate<PageResult<List<TaskListResultVO>>>() {
            @Override
            protected PageResult<List<TaskListResultVO>> process() {
                return flinkSqlTaskService.getTaskList(taskSearchVO);
            }
        }.execute();
    }

    @ApiOperation("获取各个状态任务的数量")
    @PostMapping(value = "getStatusCount")
    public R<Map<String, Integer>> getStatusCount(@RequestBody TaskStatusSearchVO taskStatusSearchVO) {
        return new APITemplate<Map<String, Integer>>() {
            @Override
            protected Map<String, Integer> process() {
                return flinkSqlTaskService.getStatusCountByCondition(taskStatusSearchVO);
            }
        }.execute();
    }

    @ApiOperation("FlinkSQL任务SQL格式化")
    @PostMapping(value = "sqlFormat")
    public R<String> sqlFormat(@RequestBody TaskSqlFormatVO sqlFormatVO) {
        return new APITemplate<String>() {
            @Override
            protected String process() {
                return flinkSqlTaskService.sqlFormat(sqlFormatVO.getSql());
            }
        }.execute();
    }

    @ApiOperation("获取 TaskManager 信息")
    @PostMapping(value = "listTaskManagerByTaskId")
    public R<List<FlinkSqlTaskManagerVO>> listTaskManagerByTaskId(@RequestBody TaskIdQueryVO taskIdQueryVO) {
        return new APITemplate<List<FlinkSqlTaskManagerVO>>() {
            @Override
            protected List<FlinkSqlTaskManagerVO> process() {
                flinkSqlRuntimeLogService.listTaskManagerByJobId(taskIdQueryVO.getTaskId(), taskIdQueryVO.getTenantId());
                FlinkSqlTaskManagerVO flinkSqlTaskManagerVO = new FlinkSqlTaskManagerVO();
                flinkSqlTaskManagerVO.setId("container_e82_1650723732517_8257_01_000002");
                return Arrays.asList(flinkSqlTaskManagerVO);
            }
        }.execute();
    }

    @ApiOperation("获取 Master 节点日志信息")
    @PostMapping(value = "getJobManagerLog")
    public R<RuntimeLogResultVO> getJobManagerLog(@RequestBody RuntimeLogQueryVO logQueryVO) {
        return new APITemplate<RuntimeLogResultVO>() {
            @Override
            protected RuntimeLogResultVO process() {
                return flinkSqlRuntimeLogService.getJobManagerLog(logQueryVO.getTaskId(), logQueryVO.getPlace(), logQueryVO.getTenantId());
            }
        }.execute();
    }

    @ApiOperation("获取 Worker 节点日志信息")
    @PostMapping(value = "getTaskManagerLog")
    public R<RuntimeLogResultVO> getTaskManagerLog(@RequestBody RuntimeLogQueryVO logQueryVO) {
        return new APITemplate<RuntimeLogResultVO>() {
            @Override
            protected RuntimeLogResultVO process() {
                return flinkSqlRuntimeLogService.getTaskManagerLog(logQueryVO.getTaskId(), logQueryVO.getTaskManagerId(), logQueryVO.getCurrentPage(),
                        logQueryVO.getPlace(), logQueryVO.getTenantId());
            }
        }.execute();
    }

    @ApiOperation("根据任务 ID 获取日志信息")
    @PostMapping(value = "getLogsByTaskId")
    public R<FlinkServerLogVO> getLogsByTaskId(@RequestBody ServerLogsVO vo) {
        return new APITemplate<FlinkServerLogVO>() {
            @Override
            protected FlinkServerLogVO process() {
              return   flinkServerLogService.getLogsByTaskId(vo);
            }
        }.execute();
    }

    @ApiOperation("根据任务 ID 获取失败日志信息")
    @PostMapping(value = "getFailoverLogsByTaskId")
    public R<String> getFailoverLogsByTaskId(@RequestBody GetFailoverLogsByTaskIdVO vo) {
        return new APITemplate<String>() {
            @Override
            protected String process() {
                return "error";
            }
        }.execute();
    }

    @ApiOperation(value = "获取任务历史信息")
    @PostMapping(value = "getJobHistoryList")
    public R<PageResult<List<ScheduleJobHistory>>> getJobHistoryList(@RequestBody @Validated TaskJobHistorySearchVO jobHistorySearchVO) {
        return new APITemplate<PageResult<List<ScheduleJobHistory>>>() {
            @Override
            protected PageResult<List<ScheduleJobHistory>> process() {
                return flinkSqlRuntimeLogService.getHistoryList(jobHistorySearchVO.getTaskId(), 0, 1);
            }
        }.execute();
    }
}
