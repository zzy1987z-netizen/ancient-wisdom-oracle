
import sys
from memory_core import log_memory

def main():
    """脚本主入口，用于从命令行接收对话并记录"""
    # 命令行参数应该包含两部分: [1]说话人(user/ai) [2]对话内容
    if len(sys.argv) != 3:
        print("Usage: python log_chat.py <speaker> \"<text>\"")
        print("Example: python log_chat.py user \"Hello, world!\"")
        return

    speaker = sys.argv[1]
    text = sys.argv[2]

    if speaker not in ["user", "ai", "system"]:
        print(f"Error: Invalid speaker '{speaker}'. Must be 'user', 'ai', or 'system'.")
        return

    success, message = log_memory(speaker, text)

    if success:
        # 成功时静默，以便于自动化调用
        pass
    else:
        # 失败时打印错误信息
        print(f"Error logging memory: {message}")

if __name__ == "__main__":
    main()

