
import json
import os
import datetime

# --- 核心配置 ---
MEMORY_FILE = "D:/lzlq/laoyouzhuanshu/our_memory.json"

# --- 记忆结构定义 ---
class MemoryEntry:
    def __init__(self, speaker, text, session_id="default"):
        self.timestamp = datetime.datetime.now().isoformat()
        self.speaker = speaker  # "user" or "ai"
        self.text = text
        self.session_id = session_id
        self.vector = []  # 预留给未来的向量

    def to_dict(self):
        return {
            "timestamp": self.timestamp,
            "speaker": self.speaker,
            "session_id": self.session_id,
            "text": self.text,
            "vector": self.vector
        }

# --- 核心功能：记录记忆 ---
def log_memory(speaker, text, session_id="default"):
    """将一条对话记录到记忆文件中"""
    new_entry = MemoryEntry(speaker, text, session_id)

    # 读取现有记忆
    if os.path.exists(MEMORY_FILE):
        try:
            with open(MEMORY_FILE, "r", encoding="utf-8") as f:
                data = json.load(f)
        except (json.JSONDecodeError, FileNotFoundError):
            data = []
    else:
        data = []

    # 追加新记忆并写回
    data.append(new_entry.to_dict())

    try:
        with open(MEMORY_FILE, "w", encoding="utf-8") as f:
            json.dump(data, f, ensure_ascii=False, indent=4)
        return True, "Memory logged successfully."
    except IOError as e:
        return False, f"Failed to write memory file: {e}"

# --- 测试入口 ---
if __name__ == '__main__':
    # 这是一个测试，当你直接运行这个文件时会被触发
    print("正在测试记忆核心模块...")
    log_memory("system", "Memory core initialized.")
    log_memory("user", "这是我的第一句话。")
    log_memory("ai", "这是我的第一句回应。")
    print(f"测试完成，请检查文件: {MEMORY_FILE}")

