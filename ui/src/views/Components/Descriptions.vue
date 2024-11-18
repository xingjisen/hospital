<script setup lang="tsx">
import {Descriptions, DescriptionsSchema} from '@/components/Descriptions'
import {useI18n} from '@/hooks/web/useI18n'
import {reactive} from 'vue'
import {Form} from '@/components/Form'
import {ElFormItem, ElInput} from 'element-plus'
import {useValidator} from '@/hooks/web/useValidator'
import {useForm} from '@/hooks/web/useForm'

const {required} = useValidator()

const {t} = useI18n()

const data = reactive({
    userName: 'chenkl',
    nickName: '梦似花落。',
    age: 26,
    phone: '13655971xxxx',
    email: '502431556@qq.com',
    addr: '这是一个很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长的地址',
    sex: '男',
    certy: '3505831994xxxxxxxx'
})

const schema = reactive<DescriptionsSchema[]>([
    {
        field: 'userName',
        label: t('descriptionsDemo.userName')
    },
    {
        field: 'nickName',
        label: t('descriptionsDemo.nickName')
    },
    {
        field: 'phone',
        label: t('descriptionsDemo.phone')
    },
    {
        field: 'email',
        label: t('descriptionsDemo.email')
    },
    {
        field: 'addr',
        label: t('descriptionsDemo.addr'),
        span: 24
    }
])

const schema2 = reactive<DescriptionsSchema[]>([
    {
        field: 'userName',
        label: t('descriptionsDemo.userName'),
        slots: {
            label: (row) => {
                return <span class="is-required--item">{row.label}</span>
            },
            default: () => {
                return (
                    <ElFormItem prop="userName">
                        <ElInput v-model={form.userName}/>
                    </ElFormItem>
                )
            }
        }
    },
    {
        field: 'nickName',
        label: t('descriptionsDemo.nickName'),
        slots: {
            label: (row) => {
                return <span class="is-required--item">{row.label}</span>
            },
            default: () => {
                return (
                    <ElFormItem prop="nickName">
                        <ElInput v-model={form.nickName}/>
                    </ElFormItem>
                )
            }
        }
    },
    {
        field: 'phone',
        label: t('descriptionsDemo.phone'),
        slots: {
            label: (row) => {
                return <span class="is-required--item">{row.label}</span>
            },
            default: () => {
                return (
                    <ElFormItem prop="phone">
                        <ElInput v-model={form.phone}/>
                    </ElFormItem>
                )
            }
        }
    },
    {
        field: 'email',
        label: t('descriptionsDemo.email'),
        slots: {
            label: (row) => {
                return <span class="is-required--item">{row.label}</span>
            },
            default: () => {
                return (
                    <ElFormItem prop="email">
                        <ElInput v-model={form.email}/>
                    </ElFormItem>
                )
            }
        }
    },
    {
        field: 'addr',
        label: t('descriptionsDemo.addr'),
        slots: {
            label: (row) => {
                return <span class="is-required--item">{row.label}</span>
            },
            default: () => {
                return (
                    <ElFormItem prop="addr">
                        <ElInput v-model={form.addr}/>
                    </ElFormItem>
                )
            }
        },
        span: 24
    }
])

const form = reactive({
    userName: '',
    nickName: '',
    phone: '',
    email: '',
    addr: ''
})

const rules = reactive({
    userName: [required()],
    nickName: [required()],
    phone: [required()],
    email: [required()],
    addr: [required()]
})

const {formRegister, formMethods} = useForm()
const {getElFormExpose} = formMethods

const formValidation = async () => {
    const elFormExpose = await getElFormExpose()
    elFormExpose?.validate((isValid) => {
        console.log(isValid)
    })
}
</script>

<template>
    <Descriptions
            :title="t('descriptionsDemo.descriptions')"
            :message="t('descriptionsDemo.descriptionsDes')"
            :data="data"
            :schema="schema"
    />

    <Form is-custom :model="form" :rules="rules" @register="formRegister">
        <Descriptions
                :title="t('descriptionsDemo.form')"
                :data="data"
                :schema="schema2"
                class="mt-20px"
        />
        <div class="text-center mt-10px">
            <BaseButton @click="formValidation"> {{ t('formDemo.formValidation') }}</BaseButton>
        </div>
    </Form>
</template>

<style lang="less" scoped>
:deep(.is-required--item) {
  position: relative;

  &::before {
    margin-right: 4px;
    color: var(--el-color-danger);
    content: '*';
  }
}
</style>
