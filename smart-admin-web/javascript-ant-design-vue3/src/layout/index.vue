<!--
  *  layout 多种模式
  * 
  * @Author:    YY Studio 
  * @Date:      2022-09-06 20:40:16 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  YY Studio 
-->
<template>
  <!--左侧菜单 模式-->
  <SideLayout v-if="layout === LAYOUT_ENUM.SIDE.value" />
  <!--左侧展开菜单 模式-->
  <SideExpandLayout v-if="layout === LAYOUT_ENUM.SIDE_EXPAND.value" />
  <!--顶部菜单 模式-->
  <TopLayout v-if="layout === LAYOUT_ENUM.TOP.value" />
</template>
<script setup>
import { computed } from 'vue';
import { LAYOUT_ENUM } from '/@/constants/layout-const';
import SideExpandLayout from './side-expand-layout.vue';
import SideLayout from './side-layout.vue';
import TopLayout from './top-layout.vue';
import { useAppConfigStore } from '/@/store/modules/system/app-config';

const layout = computed(() => useAppConfigStore().$state.layout);
</script>
