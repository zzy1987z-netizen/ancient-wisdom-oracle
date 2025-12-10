
import os
import shutil
import time

# --- 脚本配置 ---
# 项目根目录，请勿修改
PROJECT_ROOT = "D:/lzlq"

# 需要删除的文件列表
FILES_TO_DELETE = [
    "一键启动.bat",
    "虚拟环境.bat",
    "jiyiku.py",
    "jiantingyuan.py",
    "moxingdiaoyong.py",
    "qianduan.py",
    "shouhu.py",
    "zhilingxieyi.py",
    "zhukong.py",
    "插件模拟测试.py",
    "插件日志分析器.py",
    "memory.db",
    "memory_store.json",
    "plugin_log.txt",
    "新窗口使用说明书.txt",
    "新建文本文档.txt"
]

# 需要删除的文件夹列表
DIRS_TO_DELETE = [
    "chajian",
    "models",
    "__pycache__"
]

def main():
    """主执行函数"""
    print("="*50)
    print("🧹 旧文件清理脚本已启动")
    print(f"🎯 目标根目录: {PROJECT_ROOT}")
    print("="*50)

    # 切换到项目根目录
    os.chdir(PROJECT_ROOT)

    print("\n📜 将要删除以下【文件】:")
    for f in FILES_TO_DELETE:
        if os.path.exists(f):
            print(f"  - {f}")

    print("\n📂 将要删除以下【文件夹】及其全部内容:")
    for d in DIRS_TO_DELETE:
        if os.path.exists(d):
            print(f"  - {d}")

    print("\n" + "="*50)
    print("🚨【警告】🚨")
    print("此操作不可逆！以上所有列出的文件和文件夹将被永久删除！")
    print("="*50)

    try:
        confirm = input("\n❓ 是否确认执行删除操作? (请输入 y 然后按回车确认): ")
    except KeyboardInterrupt:
        print("\n🚫 用户取消操作。")
        return

    if confirm.lower() == 'y':
        print("\n🚀 开始执行删除...")
        
        # 删除文件
        for f in FILES_TO_DELETE:
            try:
                if os.path.exists(f):
                    os.remove(f)
                    print(f"  ✅ 文件已删除: {f}")
            except OSError as e:
                print(f"  ❌ 删除文件失败: {f} -> {e}")

        # 删除文件夹
        for d in DIRS_TO_DELETE:
            try:
                if os.path.exists(d):
                    shutil.rmtree(d)
                    print(f"  ✅ 文件夹已删除: {d}")
            except OSError as e:
                print(f"  ❌ 删除文件夹失败: {d} -> {e}")
        
        print("\n🎉 清理完成！")

    else:
        print("\n🚫 操作已取消。没有删除任何文件。")

if __name__ == "__main__":
    main()
    # 在退出前暂停3秒，让用户能看到结果
    time.sleep(3)

