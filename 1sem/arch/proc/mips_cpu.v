`include "util.v"

module mips_cpu(clk, pc, pc_new, instruction_memory_a, instruction_memory_rd, data_memory_a, data_memory_rd, data_memory_we, data_memory_wd,
                register_a1, register_a2, register_a3, register_we3, register_wd3, register_rd1, register_rd2);
    // сигнал синхронизации
    input clk;
    // текущее значение регистра PC
    inout [31:0] pc;
    // новое значение регистра PC (адрес следующей команды)
    output reg [31:0] pc_new;
    // we для памяти данных
    output reg data_memory_we;
    // адреса памяти и данные для записи памяти данных
    output reg [31:0] instruction_memory_a, data_memory_a, data_memory_wd;
    // данные, полученные в результате чтения из памяти
    inout [31:0] instruction_memory_rd, data_memory_rd;
    // we3 для регистрового файла
    output reg register_we3;
    // номера регистров
    output reg [4:0] register_a1, register_a2, register_a3;
    // данные для записи в регистровый файл
    output reg [31:0] register_wd3;
    // данные, полученные в результате чтения из регистрового файла
    inout [31:0] register_rd1, register_rd2;
    reg [5:0] opcode;
    reg [4:0] rs, rt;
    reg [15:0] imm;
    reg [4:0] rd, shamt;
    reg [5:0] funct;
    reg signed [31:0] signImm, read1, read2;
    always @* begin
        instruction_memory_a = pc;
        opcode = instruction_memory_rd[31:26];
        rs = instruction_memory_rd[25:21];
        register_a1 = rs;
        rt = instruction_memory_rd[20:16];
        register_a2 = rt;
        imm = instruction_memory_rd[15:0];
        rd = instruction_memory_rd[15:11];
        shamt = instruction_memory_rd[10:6];
        funct = instruction_memory_rd[5:0];
        signImm = {{16{imm[15]}}, imm};
        read1 = register_rd1;
        read2 = register_rd2;
        data_memory_wd = read2;
        if (opcode == 6'b100011) begin
            data_memory_a = signImm + read1;
            register_a3 = rt;
            register_wd3 = data_memory_rd;
            data_memory_we = 0;
            register_we3 = 1;
            pc_new = pc + 4;
        end
        else if (opcode == 6'b101011) begin
            data_memory_a = signImm + read1;
            register_a3 = 0;
            register_wd3 = 0;
            data_memory_we = 1;
            register_we3 = 0;
            pc_new = pc + 4;
        end
        else if (opcode == 6'b000100) begin
            data_memory_a = 0;
            register_a3 = 0;
            register_wd3 = 0;
            data_memory_we = 0;
            register_we3 = 0;
            if (read1 == read2) pc_new = pc + (signImm + 1) * 4;
            else pc_new = pc + 4;
        end
        else if (opcode == 6'b001000) begin
            data_memory_a = 0;
            register_a3 = rt;
            register_wd3 = read1 + signImm;
            data_memory_we = 0;
            register_we3 = 1;
            pc_new = pc + 4;
        end
        else if (opcode == 6'b001100) begin
            data_memory_a = 0;
            register_a3 = rt;
            register_wd3 = read1 & signImm;
            data_memory_we = 0;
            register_we3 = 1;
            pc_new = pc + 4;
        end
        else if (opcode == 6'b000101) begin
            data_memory_a = 0;
            register_a3 = 0;
            register_wd3 = 0;
            data_memory_we = 0;
            register_we3 = 0;
            if (read1 != read2) pc_new = pc + (signImm + 1) * 4;
            else pc_new = pc + 4;
        end
        else begin
            if (funct == 6'b100000) register_wd3 = read1 + read2;
            else if (funct == 6'b100010) register_wd3 = read1 - read2;
            else if (funct == 6'b100100) register_wd3 = read1 & read2;
            else if (funct == 6'b100101) register_wd3 = read1 | read2;
            else register_wd3 = read1 < read2;
            data_memory_a = 0;
            register_a3 = rd;
            data_memory_we = 0;
            register_we3 = 1;
            pc_new = pc + 4;
        end
    end

endmodule