<template>
    <!-- Query Form -->
    <a-form class="smart-query-form">
        <a-row class="smart-query-form-row">
            <a-form-item label="项目编号" class="smart-query-form-item">
                <a-input style="width: 150px" v-model:value="queryForm.projectNo" placeholder="项目编号" />
            </a-form-item>
            <a-form-item label="项目类型" class="smart-query-form-item">
                <SmartEnumSelect width="190px" v-model:value="queryForm.projectType" enumName="PROJECT_TYPE_LAB_ENUM"
                    placeholder="项目类型" />
            </a-form-item>
            <a-form-item label="项目分类" class="smart-query-form-item">
                <template v-if="queryForm.projectType == 21">
                    <SmartEnumSelect width="150px" v-model:value="queryForm.category" enumName="LAB_CATEGORY_ENUM"
                        placeholder="项目分类" :disabled="!queryForm.projectType" />
                </template>
                <template v-else>
                    <SmartEnumSelect width="150px" v-model:value="queryForm.category" enumName="PROJECT_CATEGORY_ENUM"
                        placeholder="项目分类" :disabled="!queryForm.projectType" />
                </template>
            </a-form-item>
            <a-form-item label="客户" class="smart-query-form-item">
                <CustomerSelect width="200px" v-model:value="queryForm.customerId" placeholder="请选择客户" />
            </a-form-item>
            <a-form-item label="来源分类" class="smart-query-form-item">
                <SmartEnumSelect width="150px" v-model:value="queryForm.sourceType" enumName="SOURCE_TYPE_ENUM"
                    placeholder="来源分类" />
            </a-form-item>
            <a-form-item label="来源" class="smart-query-form-item">
                <template v-if="queryForm.sourceType === 1">
                    <ThirdPartySelect width="150px" v-model:value="queryForm.sourceId" placeholder="请选择咨询机构"
                        type="THIRD_3" />
                </template>
                <template v-else-if="queryForm.sourceType === 2">
                    <EmployeeSelect width="150px" v-model:value="queryForm.sourceId" placeholder="请选择内部员工" />
                </template>
                <template v-else>
                    <ThirdPartySelect width="150px" v-model:value="queryForm.sourceId" placeholder="请选择咨询机构"
                        type="THIRD_3" />
                </template>
            </a-form-item>
            <a-form-item label="操作人" class="smart-query-form-item">
                <EmployeeSelect width="150px" v-model:value="queryForm.operateUserId" placeholder="请选择内部员工" />
            </a-form-item>
            <a-form-item label="状态" class="smart-query-form-item">
                <SmartEnumSelect width="150px" v-model:value="queryForm.status" enumName="PROJECT_STATUS_ENUM"
                    placeholder="请选择状态" />
            </a-form-item>
            <a-form-item label="试验室" class="smart-query-form-item">
                <ThirdPartySelect width="150px" v-model:value="queryForm.thirdPartyId" placeholder="请选择试验室"
                    type="THIRD_1" />
            </a-form-item>
            <a-form-item label="试验室合同" class="smart-query-form-item">
                <a-input style="width: 150px" v-model:value="queryForm.labContractNo" placeholder="试验室合同" />
            </a-form-item>
            <a-form-item label="是否付款" class="smart-query-form-item">
                <SmartEnumSelect width="150px" v-model:value="queryForm.isPaid" enumName="FLAG_NUMBER_ENUM"
                    placeholder="请选择是否付款" />
            </a-form-item>
            <a-form-item label="付款方" class="smart-query-form-item">
                <SmartEnumSelect width="150px" v-model:value="queryForm.payParty" enumName="PAY_PARTY_ENUM"
                    placeholder="请选择付款方" />
            </a-form-item>
            <a-form-item label="试验费付款日期" class="smart-query-form-item">
                <a-range-picker v-model:value="labPayDateRange" style="width: 250px" @change="onLabPayDateChange" />
            </a-form-item>
            <a-form-item label="产品名称" class="smart-query-form-item">
                <a-input width="150px" v-model:value="queryForm.productName" placeholder="产品名称" />
            </a-form-item>
            <a-form-item label="产品型号" class="smart-query-form-item">
                <a-input width="150px" v-model:value="queryForm.productModel" placeholder="产品型号" />
            </a-form-item>
            <a-form-item class="smart-query-form-item">
                <a-button type="primary" @click="queryData">
                    <template #icon>
                        <SearchOutlined />
                    </template>
                    查询
                </a-button>
                <a-button @click="resetQuery" class="smart-margin-left10">
                    <template #icon>
                        <ReloadOutlined />
                    </template>
                    重置
                </a-button>
            </a-form-item>
        </a-row>
    </a-form>

    <!-- Data Table -->
    <a-card size="small" :bordered="false" :hoverable="true">
        <!---------- 表格操作行 begin ----------->
        <a-row class="smart-table-btn-block">
            <div class="smart-table-operate-block">
                <a-button @click="exportExcel()" type="primary" size="small" v-privilege="'business:lab:excel'">
                    <template #icon>
                        <FileExcelOutlined />
                    </template>
                    导出数据
                </a-button>
                <a-button @click="exportLabProductExcel()" type="primary" size="small"
                    v-privilege="'business:lab:excel'">
                    <template #icon>
                        <FileExcelOutlined />
                    </template>
                    导出试验项目数据
                </a-button>

            </div>
            <div class="smart-table-setting-block">
                <TableOperator v-model="columns" :tableId="null" :refresh="queryData" />
            </div>
        </a-row>
        <!---------- 表格操作行 end ----------->
        <a-table size="small" :dataSource="tableData" :columns="filteredColumns" @resizeColumn="handleResizeColumn"
            rowKey="id" bordered :loading="tableLoading" :pagination="false" :scroll="{ x: 2000, y: tableScrollY }">
            <template #bodyCell="{ text, record, column }">
                <template v-if="column.dataIndex === 'taskNo'">
                    <a @click="detailTask(record)">{{ record.taskNo }}</a>
                </template>
                <template v-else-if="column.dataIndex === 'projectType'">
                    <span>{{ $smartEnumPlugin.getDescByValue('PROJECT_TYPE_LAB_ENUM', text) }}</span>
                </template>
                <template v-else-if="column.dataIndex === 'category'">
                    <span>{{ getEnumDescription(record.projectType, text) }}</span>
                </template>
                <template v-else-if="column.dataIndex === 'sourceType'">
                    <span>{{ $smartEnumPlugin.getDescByValue('SOURCE_TYPE_ENUM', text) }}</span>
                </template>
                <template v-else-if="column.dataIndex === 'isPaid'">
                    <span>{{ text ? '是' : '否' }}</span>
                </template>
                <template v-else-if="column.dataIndex === 'payParty'">
                    <span>{{ $smartEnumPlugin.getDescByValue('PAY_PARTY_ENUM', text) }}</span>
                </template>
                <template v-else-if="column.dataIndex === 'projectStatus'">
                    <span>{{ $smartEnumPlugin.getDescByValue('PROJECT_STATUS_ENUM', text) }}</span>
                </template>
                <template v-else-if="column.dataIndex === 'action'">
                    <div class="smart-table-operate">
                        <a-dropdown>
                            <a class="ant-dropdown-link" @click.prevent>
                                节点操作
                            </a>
                            <template #overlay>
                                <a-menu @click="handleMenuClick($event, record)">
                                    <a-menu-item>
                                        产品列表
                                    </a-menu-item>
                                    <a-menu-divider />
                                    <a-menu-item v-for="node in record.projectNodeList" :key="node">
                                        {{ node.nodeName }}
                                    </a-menu-item>
                                </a-menu>
                            </template>
                        </a-dropdown>
                    </div>
                </template>
            </template>
        </a-table>

        <div class="smart-query-table-page">
            <a-pagination showSizeChanger showQuickJumper show-less-items :pageSizeOptions="PAGE_SIZE_OPTIONS"
                :defaultPageSize="queryForm.pageSize" v-model:current="queryForm.pageNum"
                v-model:pageSize="queryForm.pageSize" :total="total" @change="queryData" @showSizeChange="queryData"
                :show-total="total => `共${total}条`" />
        </div>


        <EnterContractForm ref="enterContractFormRef" @reloadList="queryData" />
        <SendDataForm ref="sendDataFormRef" @reloadList="queryData" />
        <ReceiveDataForm ref="receiveDataFormRef" @reloadList="queryData" />
        <PayExperimentFeeForm ref="payExperimentFeeFormRef" @reloadList="queryData" />
        <AssignTaskForm ref="assignTaskFormRef" @reloadList="queryData" />
        <EstimateCompletionForm ref="estimateCompletionFormRef" @reloadList="queryData" />
        <ExperimentCheckForm ref="experimentCheckFormRef" @reloadList="queryData" />
    </a-card>
</template>

<script setup>
import { reactive, ref, onMounted, getCurrentInstance, onBeforeUnmount, watch, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { projectLabApi } from '/@/api/business/project/project-lab-api';
import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
import TableOperator from '/@/components/support/table-operator/index.vue';
import SmartEnumSelect from '/@/components/framework/smart-enum-select/index.vue';
import CustomerSelect from '/@/components/business/project/customer-select/index.vue';
import ThirdPartySelect from '/@/components/business/project/third-party-select/index.vue';
import EmployeeSelect from '/@/components/system/employee-select/index.vue';
import { SearchOutlined, ReloadOutlined } from '@ant-design/icons-vue';
import NODE_CONST from '/@/constants/business/project/node-const';
import EnterContractForm from '../common-nodes/enter-contract/enter-contract-form.vue';
import SendDataForm from '../common-nodes/send-data/send-data-form.vue';
import ReceiveDataForm from '../common-nodes/receive-data/receive-data-form.vue';
import PayExperimentFeeForm from '../common-nodes/pay-experiment-fee/pay-experiment-fee-form.vue';
import AssignTaskForm from '../common-nodes/assign-task/assign-task-form.vue';
import EstimateCompletionForm from '../common-nodes/estimate-completion/estimate-completion-form.vue';
import ExperimentCheckForm from '../common-nodes/experiment-check/experiment-check-form.vue';

// Columns for the table based on ProjectLabListVO
const columns = ref([
    { title: '试验室任务编号', dataIndex: 'taskNo', fixed: 'left', width: 170 },
    { title: '客户', dataIndex: 'customerName', fixed: 'left', width: 200 },
    { title: '状态', dataIndex: 'projectStatus', fixed: 'left', width: 70 },
    { title: '项目编号', dataIndex: 'projectNo', width: 150 },
    { title: '项目类型', dataIndex: 'projectType', width: 120 },
    { title: '项目分类', dataIndex: 'category', width: 80 },
    { title: '来源分类', dataIndex: 'sourceType', width: 80 },
    { title: '来源', dataIndex: 'sourceName', width: 120 },
    { title: '试验室名称', dataIndex: 'thirdPartyName', width: 120 },
    { title: '试验室合同号', dataIndex: 'labContractNo', width: 120 },
    { title: '试验室合同日期', dataIndex: 'labContractDate', width: 120 },
    { title: '试验费金额', dataIndex: 'labContractAmount', width: 100 },
    { title: '客户要求完成日期', dataIndex: 'labExpectedDate', width: 130 },
    { title: '资料发送日期', dataIndex: 'dataSendDate', width: 120 },
    { title: '资料接收日期', dataIndex: 'dataReceiveDate', width: 120 },
    { title: '是否付款', dataIndex: 'isPaid', width: 80 },
    { title: '付款方', dataIndex: 'payParty', width: 80 },
    { title: '试验费付款日期', dataIndex: 'labPayDate', width: 120 },
    { title: '试验室下达任务', dataIndex: 'assignTaskDate', width: 120 },
    { title: '预计完成日期', dataIndex: 'expectedCompletionDate', width: 120 },
    { title: '报告完成日期', dataIndex: 'reportCompletionDate', width: 120 },
    { title: '自我声明日期', dataIndex: 'selfDeclarationDate', width: 120 },
    { title: '证书发送日期', dataIndex: 'certificateSendDate', width: 120 },
    { title: '邮寄日期', dataIndex: 'mailingDate', width: 120 },
    { title: '创建时间', dataIndex: 'createTime', width: 170 },
    { title: '操作', dataIndex: 'action', fixed: 'right', width: 90 },
].map(column => ({ ...column, resizable: true })));

const filteredColumns = computed(() => {
    if (queryForm.projectType === 21) {
        return columns.value.filter(column =>
            column.dataIndex !== 'selfDeclarationDate' && column.dataIndex !== 'certificateSendDate'
        );
    }
    return columns.value;
});

// Query form state
const queryFormState = {
    projectNo: undefined,
    projectType: undefined,
    customerId: undefined,
    sourceType: undefined,
    sourceId: undefined,
    operateUserId: undefined,
    status: undefined,
    thirdPartyId: undefined,
    labContractNo: undefined,
    isPaid: undefined,
    payParty: undefined,
    labPayDateBegin: undefined,
    labPayDateEnd: undefined,
    productName: undefined,
    productModel: undefined,
    pageNum: 1,
    pageSize: 10,
};

// Reactive query form
const queryForm = reactive({ ...queryFormState });
const labPayDateRange = ref([]);

// Table loading state
const tableLoading = ref(false);
const tableData = ref([]);
const total = ref(0);

// Router and route
const router = useRouter();
const route = useRoute();

const enterContractFormRef = ref();
const sendDataFormRef = ref();
const receiveDataFormRef = ref();
const payExperimentFeeFormRef = ref();
const assignTaskFormRef = ref();
const estimateCompletionFormRef = ref();
const experimentCheckFormRef = ref();

// Query data
const queryData = async () => {
    tableLoading.value = true;
    try {
        const { data } = await projectLabApi.queryLabListPage(queryForm);
        tableData.value = data.list;
        total.value = data.total;
    } catch (error) {
        console.error(error);
    } finally {
        tableLoading.value = false;
    }
};

// 获取枚举描述的方法
const getEnumDescription = (projectType, value) => {
    const enumName = projectType === 21 ? 'LAB_CATEGORY_ENUM' : 'PROJECT_CATEGORY_ENUM';
    const internalInstance = getCurrentInstance(); // 有效  全局
    const smartEnumPlugin = internalInstance.appContext.config.globalProperties.$smartEnumPlugin;
    return smartEnumPlugin.getDescByValue(enumName, value);
};

// Reset query form
const resetQuery = () => {
    Object.assign(queryForm, queryFormState);
    labPayDateRange.value = [];
    queryData();
};

// Handle date change for lab pay date
const onLabPayDateChange = (dates, dateStrings) => {
    queryForm.labPayDateBegin = dateStrings[0];
    queryForm.labPayDateEnd = dateStrings[1];
};

// Handle menu click
const handleMenuClick = (e, param) => {
    let orgProjectType = param.projectType;
    param.projectType = 111;
    if (e.key === null) {
        router.push({
            path: '/project/product-list', query: {
                projectId: route.query.projectId,
                customerName: param.customerName, projectType: orgProjectType, category: param.category,
                taskId: param.id
            }
        });
    } else if (e.key.nodeId === NODE_CONST.enter_contract) {
        enterContractFormRef.value.show(param, e.key.id);
    } else if (e.key.nodeId === NODE_CONST.send_data) {
        sendDataFormRef.value.show(param, e.key.id);
    } else if (e.key.nodeId === NODE_CONST.receive_data) {
        receiveDataFormRef.value.show(param, e.key.id);
    } else if (e.key.nodeId === NODE_CONST.pay_experiment_fee) {
        payExperimentFeeFormRef.value.show(param, e.key.id);
    } else if (e.key.nodeId === NODE_CONST.assign_task) {
        assignTaskFormRef.value.show(param, e.key.id);
    } else if (e.key.nodeId === NODE_CONST.estimate_completion) {
        estimateCompletionFormRef.value.show(param, e.key.id);
    } else if (e.key.nodeId === NODE_CONST.experiment_check) {
        experimentCheckFormRef.value.show(param, e.key.id);
    }

};

// Detail navigation
const detail = (id) => {
    router.push({ path: `/project/project-detail/${id}` });
};

function detailTask(record) {
    router.push({ path: '/project/lab-detail', query: { id: record.id, customerName: record.customerName, projectType: record.projectType, category: record.category } });
}

// --------------------------- 导出 ---------------------------
async function exportExcel() {
    await projectLabApi.exportExcel(queryForm);
}

async function exportLabProductExcel() {
    await projectLabApi.exportLabProductExcel(queryForm);
}


function handleResizeColumn(w, col) {
    col.width = w;
}

const tableScrollY = ref(600);
const updateTableScrollY = () => {
    const headerHeight = 240; // 假设的头部高度，根据实际情况调整
    const otherElementsHeight = 0; // 其他元素的总高度，根据实际情况调整
    tableScrollY.value = window.innerHeight - headerHeight - otherElementsHeight;
};

onMounted(() => {
    queryData();
    updateTableScrollY();
    window.addEventListener('resize', updateTableScrollY);
});

onBeforeUnmount(() => {
    window.removeEventListener('resize', updateTableScrollY);
});
watch(() => queryForm.projectType, (newVal) => {
    if (!newVal) {
        queryForm.category = null;
    }
});
</script>
